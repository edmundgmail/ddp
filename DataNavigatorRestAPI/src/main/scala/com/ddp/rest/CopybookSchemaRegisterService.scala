package com.ddp.rest

import akka.actor.{Actor, Props}
import spray.http._
import spray.http.MediaTypes._
import spray.routing._
import spray.http.BodyPart
import java.io._

import com.ddp.access.CopybookSchemaRegister
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
                                            cpyBookName : String
                                          )

object copybookSchemaRegisterJsonProtocol extends DefaultJsonProtocol {
  implicit val copybookSchemaRegisterParameterFormat = jsonFormat1(CopybookSchemaRegisterParameter)
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
                  if(key.equals("param"))
                    key -> entity.asString.parseJson.convertTo[CopybookSchemaRegisterParameter]
                  else if(key.equals("cpybook"))
                    key->entity.data.toByteArray
                  else
                    key->entity.data.toByteArray

                case _ => ""->""
              } toMap
              val param =  details.get("param").get.asInstanceOf[CopybookSchemaRegisterParameter]
              val cpybook = new String(details.get(param.cpyBookName).get.asInstanceOf[Array[Byte]])
              val datafiles = details.filterKeys( key=> (!key.equals("param") && !key.equals("cpybook"))).mapValues(_.asInstanceOf[Array[Byte]])

              CopybookSchemaRegister(config, param, cpybook, datafiles).run

              s"complete"
            }
          }
        }
      }
    }
  }





}