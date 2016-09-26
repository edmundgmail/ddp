package com.ddp.rest

import java.io.OutputStream

import akka.actor.{Actor, ActorLogging, ActorRef, ActorRefFactory, ActorSystem, Props}
import akka.io.IO
import akka.util.ByteString
import spray.can.server.UHttp
import spray.can.{Http, websocket}
import spray.can.websocket.FrameCommandFailed
import spray.can.websocket.frame.{BinaryFrame, TextFrame}
import spray.http.HttpRequest
import spray.routing.HttpServiceActor


  final case class Push(msg: String)
  final case class PushToChildren(msg: String)

  object WebSocketServer {
    def props() = Props(classOf[WebSocketServer])
  }
  class WebSocketServer extends Actor with ActorLogging {
    def receive = {
      // when a new connection comes in we register a WebSocketConnection actor as the per connection handler
      case Http.Connected(remoteAddress, localAddress) =>
        val serverConnection = sender()
        val conn = context.actorOf(WebSocketWorker.props(serverConnection), "worker1")
        serverConnection ! Http.Register(conn)
      case PushToChildren(msg: String) =>
        val children = context.children
        println("pushing to all children : " + msg)
        children.foreach(ref => ref ! Push(msg))
    }
  }

  object WebSocketWorker {
    def props(serverConnection: ActorRef) = Props(classOf[WebSocketWorker], serverConnection)
  }
  class WebSocketWorker(val serverConnection: ActorRef) extends OutputStream with websocket.WebSocketServerWorker  {
    override def receive = handshaking orElse /* businessLogicNoUpgrade orElse*/closeLogic
    var buffer : Array[Byte] = new Array[Byte] (4096)
    var index  = 0

    override def write(b:Int): Unit ={
      buffer(index) = b.toByte
      index += 1
      if(index == 4096)
        flush()
    }

    override def flush(): Unit = {
        if(index > 0){
            self ! BinaryFrame.apply(ByteString.fromArray(buffer, 0, index))
            index = 0
        }
    }

    override def write(b: Array[Byte]): Unit = {
      write(b, 0, b.length)
    }

    override def write(b: Array[Byte], off: Int, len: Int): Unit = {

        flush()

        self ! BinaryFrame.apply(ByteString.fromArray(b, off, len))

    }

    def businessLogic: Receive = {
      // just bounce frames back for Autobahn testsuite
      case x @ (_: BinaryFrame | _: TextFrame) =>
        //sender() ! x
        log.info("x=" + x)


      case Push(msg) => send(TextFrame(msg + "back from server"))

      case x: FrameCommandFailed =>
        log.error("frame command failed", x)

      case x: HttpRequest =>  log.info("recevied ,x = " + x)
    }

   // def businessLogicNoUpgrade: Receive = {
     // implicit val refFactory: ActorRefFactory = context
     // runRoute {
     //   getFromResourceDirectory("webapp")

    //  }
   // }
  }

