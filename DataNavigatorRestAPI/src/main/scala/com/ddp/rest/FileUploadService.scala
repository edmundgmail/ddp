package com.ddp.rest

import akka.actor.{ Props, Actor }
import spray.http._
import spray.http.MediaTypes._
import spray.routing._
import spray.http.BodyPart
import java.io.{ ByteArrayInputStream, InputStream, OutputStream }

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor

// this trait defines our service behavior independently from the service actor
trait FileUploadService extends Directives{

  // we use the enclosing ActorContext's or ActorSystem's dispatcher for our Futures and Scheduler
  //implicit def executionContext = actorRefFactory.dispatcher

  val fileUploadRoute: Route = {
    path("file") {
      post {
        respondWithMediaType(`application/json`) {
          entity(as[MultipartFormData]) { formData =>
            complete {
              val details = formData.fields.map {
                case BodyPart(entity, headers) =>
                  //val content = entity.buffer
                  val content = new ByteArrayInputStream(entity.data.toByteArray)
                  val h = headers.find(h => h.is("content-disposition"))
                  val fileName=h.get.value.split("filename=").last
                  val filetype=h.get.value.split("name=").last
                  System.out.println("filetype=" + filetype)
                  System.out.println("fileName=" + fileName)
                  filetype match{
                    case "copybook" => System.out.println("this is copybook ")
                  }
                  val result = saveAttachment("/tmp/"+fileName, content)
                case _ =>
              }
              s"""{"status": "Processed POST request, details=$details" }"""
            }
            }
          }
        }
      }
  }


  private def saveAttachment(fileName: String, content: Array[Byte]): Boolean = {
    saveAttachment[Array[Byte]](fileName, content, {(is, os) => os.write(is)})
    true
  }

  private def saveAttachment(fileName: String, content: InputStream): Boolean = {
    saveAttachment[InputStream](fileName, content,
    { (is, os) =>
      val buffer = new Array[Byte](16384)
      Iterator
        .continually (is.read(buffer))
        .takeWhile (-1 !=)
        .foreach (read=>os.write(buffer,0,read))
    }
    )
  }

  private def saveAttachment[T](fileName: String, content: T, writeFile: (T, OutputStream) => Unit): Boolean = {
    try {
      val fos = new java.io.FileOutputStream(fileName)
      writeFile(content, fos)
      fos.close()
      true
    } catch {
      case _ =>false
    }
  }


}