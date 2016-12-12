package com.ddp.rest

import akka.actor.{Actor, Props}
import spray.http._
import spray.http.MediaTypes._
import spray.routing._
import spray.http.BodyPart
import java.io._
import java.util

import spray.httpx.SprayJsonSupport._
import com.ddp.cpybook.{CopybookPreview, CopybookSchemaRegister}
import com.ddp.jdbc.{DataSourceConnection, FieldHierarchy}
import org.apache.hadoop
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import com.typesafe.config.ConfigFactory
import com.ddp.utils.Utils
import net.sf.JRecord.Common.{FieldDetail, IFieldDetail}
import net.sf.JRecord.Details.LayoutDetail
import org.xeustechnologies.jcl.{JarClassLoader, JclObjectFactory}
import spray.json.{JsObject, JsString, RootJsonFormat}

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor

// this trait defines our service behavior independently from the service actor

import spray.httpx.SprayJsonSupport._
import spray.json.DefaultJsonProtocol
import DefaultJsonProtocol._
import spray.json._
import Directives._
import scala.collection.JavaConversions._

case class CopybookSchemaRegisterParameter(
                                            cpyBookName : String
                                          )

import spray.json.DefaultJsonProtocol
import spray.json.DefaultJsonProtocol._


object copybookSchemaRegisterJsonProtocol extends DefaultJsonProtocol {
  implicit val copybookSchemaRegisterParameterFormat = jsonFormat1(CopybookSchemaRegisterParameter)

  implicit object FieldDetailFormat extends RootJsonFormat[IFieldDetail] {
    def write(f: IFieldDetail) = JsObject(Map(
      "name" -> JsString(f.getName),
      "type" -> JsNumber(f.getType)
    ))
    def read(value: JsValue): IFieldDetail = ???
  }


  implicit object LayoutDetailFormat extends RootJsonFormat[LayoutDetail] {
    def write(layout: LayoutDetail) = JsObject(Map(
      "name" -> JsString(layout.getLayoutName),
      "fields" -> JsArray(layout.getFieldNameMap.values().map(_.toJson).toList)
    ))
    def read(value: JsValue): LayoutDetail = ???
  }

}


import copybookSchemaRegisterJsonProtocol._

trait CopybookSchemaRegisterService extends Directives {

  private val config = ConfigFactory.load()

  val jclFactory: JclObjectFactory = JclObjectFactory.getInstance()
  val jcl: JarClassLoader = new JarClassLoader()

  val copybookSchemaRegisterRoute: Route = {
    path("ingestion") {
      post {
        respondWithMediaType(`application/json`) {
          entity(as[MultipartFormData]) { formData =>
            complete {
              val details = formData.fields.map {
                case BodyPart(entity, headers) =>
                  //val key = headers.find(h => h.is("content-disposition")).get.value.split(";").map(_.trim).filter(_.startsWith("name="))(0).split("name=").last
                  val key = headers(0).value.split(";").map(_.trim).find(_.startsWith("name=")).get.substring(5)
                  if (key.equals("param"))
                    key -> entity.asString.parseJson.convertTo[CopybookSchemaRegisterParameter]
                  else if (key.equals("cpybook"))
                    key -> entity.data.toByteArray
                  else
                    key -> entity.data.toByteArray

                case _ => "" -> ""
              } toMap
              val param = details.get("param").get.asInstanceOf[CopybookSchemaRegisterParameter]
              val cpybook = new String(details.get("cpybook").get.asInstanceOf[Array[Byte]])
              val datafiles = details.filterKeys(key => (!key.equals("param") && !key.equals("cpybook"))).mapValues(_.asInstanceOf[Array[Byte]])

              val layOutDetail = CopybookPreview(jclFactory, jcl, param, cpybook, datafiles).run
              //val x = s.asInstanceOf[List[IFieldDetail]].map(f=>MyFieldDetail(f.getName))

              //System.out.println("s.class=" + s.getClass + "x.class=" + x.getClass )
              layOutDetail
              }
          }
        }
      }
    }

  }
}
