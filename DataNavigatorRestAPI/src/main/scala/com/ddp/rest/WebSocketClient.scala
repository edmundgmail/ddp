package com.ddp.rest

import akka.actor.{Actor, ActorSystem, Props}
import akka.io.IO
import spray.can.server.UHttp
import spray.can.websocket.frame.Frame
import spray.can.{Http, websocket}
import spray.http.{HttpHeaders, HttpMethods, HttpRequest}

/**
  * Created by cloudera on 9/28/16.
  */
abstract class WebSocketClient(connect: Http.Connect, val upgradeRequest: HttpRequest) extends websocket.WebSocketClientWorker {

  implicit val system = akka.actor.ActorSystem("on-spray-can")

  IO(UHttp) ! connect


  def businessLogic: Receive = {
    case frame: Frame =>
      onMessage(frame)

    case _: Http.ConnectionClosed =>
      onClose()
      context.stop(self)
  }

  def onMessage(frame: Frame)

  def onClose(): Unit

}


class WebSocketClientService extends Actor{
  implicit def actorRefFactory = context
  val host = "0.0.0.0"
  val port = 8883
  val headers = List(
    HttpHeaders.Host(host, port),
    HttpHeaders.Connection("Upgrade"),
    HttpHeaders.RawHeader("Upgrade", "websocket"),
    HttpHeaders.RawHeader("Sec-WebSocket-Version", "13"),
    HttpHeaders.RawHeader("Sec-WebSocket-Key", "x3JJHMbDL1EzLkh9GBhXDw=="),
    HttpHeaders.RawHeader("Sec-WebSocket-Extensions", "permessage-deflate"))

  val connect = Http.Connect(host, port)

  val getCaseCountReq = HttpRequest(HttpMethods.GET, "/getCaseCount", headers)

  def receive:Receive = {
    case _=>
  }

  val getCaseCountClient = actorRefFactory.actorOf(Props(
    new WebSocketClient(connect, getCaseCountReq){

      var caseCount = 0

      def onMessage(frame: Frame) {
        caseCount = frame.payload.utf8String.toInt
        println("case count: " + caseCount)
      }

      def onClose() {

      }
    }
  ))

}

