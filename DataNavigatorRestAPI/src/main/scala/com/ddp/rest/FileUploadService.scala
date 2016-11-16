package com.ddp.rest

import akka.actor.{Actor, Props}
import spray.http._
import spray.http.MediaTypes._
import spray.routing._
import spray.http.BodyPart
import java.io._

import org.apache.hadoop
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import com.typesafe.config.ConfigFactory
import com.ddp.utils.Utils

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor

// this trait defines our service behavior independently from the service actor
case class UploadedPath(uploadPath: String)
import spray.json.DefaultJsonProtocol
object fileUploadJsonProtocol extends DefaultJsonProtocol {
  implicit val pathFormat = jsonFormat1(UploadedPath)
}

import fileUploadJsonProtocol._
import spray.httpx.SprayJsonSupport._

trait FileUploadService extends Directives{

  // we use the enclosing ActorContext's or ActorSystem's dispatcher for our Futures and Scheduler
  //implicit def executionContext = actorRefFactory.dispatcher
  val config = ConfigFactory.load()

  val fileUploadRoute: Route = {
    path("file") {
      post {
        respondWithMediaType(`application/json`) {
          entity(as[MultipartFormData]) { formData =>
            complete {
              val temp = Utils.getTempPath()

              val details = formData.fields.map {
                case BodyPart(entity, headers) =>
                  val content = new ByteArrayInputStream(entity.data.toByteArray)
                  val fileName = headers.find(h => h.is("content-disposition")).get.value.split("name=").last
                  //val fileName=h.find(_.startsWith("filename=")).get.split("filename=").last
                  //fileType match{
                  //  case "copybook" => {
                  val result = saveAttachment(temp+"/"+fileName, content)
                case _ =>
              }
              UploadedPath(temp)
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
      //val fos = new java.io.FileOutputStream(fileName)
      val conf = new hadoop.conf.Configuration
      conf.set("fs.defaultFS", config.getString("com.ddp.rest.defaultFS"))
      val fs = FileSystem.get (conf)
      val fos = new BufferedOutputStream(fs.create (new Path( fileName) ) )
      writeFile(content, fos)
      fos.close()
      true
    } catch {
      case _ =>false
    }
  }


}