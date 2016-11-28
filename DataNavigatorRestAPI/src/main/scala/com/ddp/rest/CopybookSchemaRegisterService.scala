package com.ddp.rest

import akka.actor.{Actor, Props}
import spray.http._
import spray.http.MediaTypes._
import spray.routing._
import spray.http.BodyPart
import java.io._

import com.ddp.jdbc.{DataSourceConnection, FieldHierarchy}
import org.apache.hadoop
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import com.typesafe.config.ConfigFactory
import com.ddp.utils.Utils

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor

// this trait defines our service behavior independently from the service actor

import spray.httpx.SprayJsonSupport._
import spray.json.DefaultJsonProtocol
import DefaultJsonProtocol._
import spray.json._

case class CopybookSchemaRegisterParameter(
                                            cpyBookName : String,
                                            cpybookFont: String = "cp037",
                                            fileStructure: String = "FixedLength",
                                            binaryFormat: String = "FMT_MAINFRAME",
                                            splitOptoin: String = "SplitNone"
                                          )

object copybookSchemaRegisterJsonProtocol extends DefaultJsonProtocol {
  implicit val copybookSchemaRegisterParameterFormat = jsonFormat5(CopybookSchemaRegisterParameter)

}

import copybookSchemaRegisterJsonProtocol._

trait CopybookSchemaRegisterService extends Directives{

  private val config = ConfigFactory.load()

  val copybookSchemaRegisterRoute: Route = {
    path("ingestion") {
      post {
        respondWithMediaType(`application/json`) {
          entity(as[MultipartFormData]) { formData =>
            complete {
              val details = formData.fields.map {
                case BodyPart(entity, headers) =>
                  val key = headers.find(h => h.is("content-disposition")).get.value.split("name=").last
                  key -> entity.data.asString.
                case _ => ""->""
              } toMap

              val param = details.get("param")
              .convertTo[CopybookSchemaRegisterParameter]
              val cpybookFile = details.get(param.cpyBookName)

              System.out.println("param=" + param.toString)
              System.out.println("cpybookFile=" + cpybookFile)
              s"Success"
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