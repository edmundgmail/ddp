package com.ddp.rest

import akka.actor.{Actor, Props}
import spray.http._
import MediaTypes._
import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import org.json4s.{DefaultFormats, Formats}
import spray.can.Http
import spray.can.server.Stats
import spray.http.StatusCodes._
import spray.httpx.Json4sSupport
import spray.routing._

import scala.concurrent.duration._

/* Used to mix in Spray's Marshalling Support with json4s */
object Json4sProtocol extends Json4sSupport {
  implicit def json4sFormats: Formats = DefaultFormats
}


// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class MyServiceActor extends Actor with MyService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(myRoute)
}




// this trait defines our service behavior independently from the service actor
trait MyService extends HttpService {
  import Json4sProtocol._
  import WorkerActor._

  implicit def executionContext = actorRefFactory.dispatcher
  implicit val timeout = Timeout(5 seconds)

  val worker = actorRefFactory.actorOf(Props[WorkerActor], "worker")

  val myRoute = {
    path("entity") {
        post {
          respondWithStatus(Created) {
            entity(as[IngestionParameter]) { someObject =>
              doCreate(someObject)
            }
          }
        } ~
        post {
          respondWithStatus(OK) {
            entity(as[QueryParameter] ) {
              someObject=>doQuery(someObject)
            }
          }
        }
    }
  }

def doQuery(param:QueryParameter) = {
  complete{
    import WorkerActor._
    (worker ? param)
      .mapTo[Ok]
      .map(result => s"I got a response: ${result}")
      .recover { case _ => "error" }
  }
}

def doCreate(param: IngestionParameter) = {
  complete {
  //We use the Ask pattern to return
  //a future from our worker Actor,
  //which then gets passed to the complete
  //directive to finish the request.
    import WorkerActor._
    (worker ? param)
      .mapTo[Ok]
      .map(result => s"I got a response: ${result}")
      .recover { case _ => "error" }
  }
}

}
