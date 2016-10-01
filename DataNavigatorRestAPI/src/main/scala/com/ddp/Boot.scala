package com.ddp

import akka.actor.{Actor, ActorLogging, ActorRef, ActorRefFactory, PoisonPill, Props}
import akka.io.IO
import spray.can.{Http, websocket}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._
import com.ddp.rest._
import spray.can.server.UHttp
import spray.can.websocket.FrameCommandFailed
import spray.can.websocket.frame.{BinaryFrame, TextFrame}
import spray.http.HttpRequest
import spray.routing.{ExceptionHandler, HttpServiceActor, RejectionHandler, RoutingSettings}
import spray.util.LoggingContext


object Boot extends App {


  // we need an ActorSystem to host our application in
  implicit val system = akka.actor.ActorSystem("on-spray-can")

  val wss = system.actorOf(Props[WebSocketServer], "wssserver1")
  // create and start our service actor
  val service = system.actorOf(Props[MyServiceActor], "demo-service1")

  implicit val timeout = Timeout(5.seconds)
  // start a new HTTP server on port 8080 with our service actor as the handler
  //IO(Http) ? Http.Bind(service, interface = "0.0.0.0", port = 8881)
  IO(UHttp) ? Http.Bind(wss, interface = "0.0.0.0", port = 8883)

  system.actorSelection("/user/IO-HTTP") ! PoisonPill

  IO(Http) ? Http.Bind(service, interface = "0.0.0.0", port = 8881)
  //service ! rest.WorkerInitialize("Hello")
  sys.addShutdownHook({ IO(UHttp) ! Http.Unbind; IO(Http) ! Http.Unbind; system.shutdown })

}
