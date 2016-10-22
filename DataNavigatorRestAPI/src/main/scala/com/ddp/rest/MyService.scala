package com.ddp.rest

import java.io.{ByteArrayInputStream, FileOutputStream}
import java.util.concurrent.Executors

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import spray.http._
import MediaTypes._
import akka.pattern.ask
import akka.util.Timeout
import com.ddp.access.DataSourceDetail
import org.json4s.{DefaultFormats, FieldSerializer, Formats}
import spray.can.{Http, websocket}
import spray.can.server.Stats
import spray.http.StatusCodes._
import spray.httpx.Json4sSupport
import spray.routing._
import com.ddp.jarmanager.{JarParamter, ScalaSourceParameter}
import com.ddp.jdbc.DataSourceConnection
import org.apache.spark.sql.{Dataset, Row}
import spray.can.websocket.FrameCommandFailed
import spray.can.websocket.frame.{BinaryFrame, TextFrame}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

/* Used to mix in Spray's Marshalling Support with json4s */
object Json4sProtocol extends Json4sSupport {
  implicit def json4sFormats: Formats = DefaultFormats
}



// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class MyServiceActor extends Actor with MyService  {



  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  implicit def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  //def receive = runRoute(myRoute)

  def receive = {
    /*
    case Http.Connected(remoteAddress, localAddress) =>
      val serverConnection = sender()
      val conn = context.actorOf(WebSocketWorker.props(serverConnection))
      serverConnection ! Http.Register(conn)
    case PushToChildren(msg: String) =>
      val children = context.children
      println("pushing to all children : " + msg)
      children.foreach(ref => ref ! Push(msg))
*/
    runRoute(myRoute ~ staticRoute)
  }

  def staticRoute: Route =
    path("")(getFromResource("app/index.html")) ~ getFromResourceDirectory("app") ~
    path("web")(getFromResource("webapp/index.html")) ~ getFromResourceDirectory("webapp")

}


// this trait defines our service behavior independently from the service actor
trait MyService extends HttpService with CorsSupport{
  import Json4sProtocol._
  import WorkerActor._
  implicit def executionContext = actorRefFactory.dispatcher
  implicit val timeout = Timeout(200 seconds)

  val worker = actorRefFactory.actorOf(Props[WorkerActor], "worker2")


  val myRoute = cors {
    path("metadata" / "connections") {
      get {
        getConnections
      }

    } ~
      path("metadata" / "dataSources") {
        get {
          parameters("conn") {
            conn => getDataSources(conn)
          }
        }
      } ~
      path("metadata" / "connHierarchy"){
        get{
          parameters ("conn") {
            conn=>getConnHierarchy(conn)
          }
        }
      } ~
      path("entity") {
        post {
          respondWithStatus(Accepted) {
            entity(as[WorkerInitialize]) { someObject =>
              doInitialize(someObject)
            }}
        } ~
        post {
          respondWithStatus(Created) {
            entity(as[CopybookIngestionParameter]) { someObject =>
              doCreate(someObject)
            }
          }
        } ~
          post {
            entity(as[QueryParameter]) {
              someObject => doQuery(someObject)

            }
          } ~
          get {
            complete("this is test")
          }

      } ~
        path("app") {
          post {
            respondWithStatus(OK) {
              entity(as[JarParamter]) { someObject =>
                doJarManager(someObject)
              }
            }
          } ~
            post {
              respondWithStatus(OK) {
                entity(as[UserClassParameter]) { someObject =>
                  doUserClass(someObject)
                }
              }
            } ~
          post {
            respondWithStatus(OK) {
              entity(as[ScalaSourceParameter]) { someObject =>
                doScalaSource(someObject)
              }
            }
         }
        } ~
    pathPrefix("css") { get { getFromResourceDirectory("app") } }~
        path("spray-html") {
          get {
            respondWithMediaType(`text/html`) {
              complete {
                <html>
                  <body>
                    <h1>Hello papa!</h1>
                  </body>
                </html>
              }
            }
          }
        }
  }

  import WorkerActor._
  //implicit val sendReceiveTimeout = akka.util.Timeout(4.minutes)
  def doScalaSource(param:ScalaSourceParameter) = {
      complete{
        (worker ? param)
          .mapTo[Ok]
          .map(result => s"I got a response: ${result}")
          .recover { case _ => "error" }
      }
    }

  def doUserClass(param:UserClassParameter) = {
    complete{
      (worker ? param)
        .mapTo[Ok]
        .map(result => s"I got a response: ${result}")
        .recover { case _ => "error" }
    }
  }

def doJarManager(param:JarParamter) = {
  complete{
    (worker ? param)
      .mapTo[Ok]
      .map(result => s"I got a response: ${result}")
      .recover { case _ => "error" }
  }
}

def doQuery(param:QueryParameter) = {
  complete{
    (worker ? param)
      .mapTo[Ok]
      .map(result => s"I got a response: ${result}")
      .recover { case _ => "error" }
  }
}

def doInitialize(param: WorkerInitialize) = {
  complete{
    //directive to finish the request.

    (worker ? param)
      .mapTo[Ok]
      .map(result => s"I got a response: ${result}")
      .recover { case _ => "error" }
  }
}

def doCreate(param: CopybookIngestionParameter) = {
  complete {
    (worker ? param)
      .mapTo[Ok]
      .map(result => s"I got a response: ${result}")
      .recover { case _ => "error" }
  }
}


def getDataSources(conn:String)= {
  import spray.json.DefaultJsonProtocol

  respondWithMediaType(`application/json`) {
    complete {
      val s = (worker ? new GetDataSources(conn))
      s
    }
  }
}

def getConnHierarchy(conn:String) = {
  import spray.json.DefaultJsonProtocol

  respondWithMediaType(`application/json`) {
    complete {
      val s = (worker ? new GetConnectionHierarchy(conn))
      s
    }
  }
}

def getConnections = {
  import spray.json.DefaultJsonProtocol
  object MyJsonProtocol extends DefaultJsonProtocol {
    implicit val dataSourceConnectionFormat = jsonFormat1(DataSourceConnection)
  }

  respondWithMediaType(`application/json`) {
    complete {
      val s = (worker ? new GetConnections).mapTo[Stream[DataSourceConnection]]
      s
    }
  }

}
}
