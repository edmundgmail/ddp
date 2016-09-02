package com.example

import akka.actor.{Actor, Props}
import spray.http._
import MediaTypes._
import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import spray.http.StatusCodes._
import spray.routing._

import scala.concurrent.duration._


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

case class Foo(bar: String)

// this trait defines our service behavior independently from the service actor
trait MyService extends HttpService {

  implicit def executionContext = actorRefFactory.dispatcher
  implicit val timeout = Timeout(5 seconds)

  val worker = actorRefFactory.actorOf(Props[WorkerActor], "worker")

  val myRoute =
    path("") {
      post {
        respondWithMediaType(`text/html`) { // XML is marshalled to `text/xml` by default, so we simply override here
          complete {
            <html>
              <body>
                <h1>Say hello to <i>spray-routing</i> on <i>spray-can</i>!</h1>
              </body>
            </html>
          }
        }
      }
    } ~
    path("create" / Segment){ className =>
        post {
          respondWithStatus(Created) {
              doCreate(Foo(className))
          }
        }
    } ~
    pathPrefix("query"){
      get{
        complete("")
      }
    }

def doCreate(foo: Foo) = {
  complete {
  //We use the Ask pattern to return
  //a future from our worker Actor,
  //which then gets passed to the complete
  //directive to finish the request.
    import WorkerActor._
    (worker ? Create(foo))
      .mapTo[Ok]
      .map(result => s"I got a response: ${result}")
      .recover { case _ => "error" }
  }
}

}