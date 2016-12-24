package com.ddp.jdbc

import com.ddp.rest.GetConnectionHierarchy
import com.ddp.rest.WorkerActor.Ok
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.calcite.rel.`type`.RelDataTypeField
import org.bson.Document

/**
  * Created by cloudera on 10/9/16.
  */


object MetaDataMongoDB{

  case class DataSourceConnection(name:String)
  case class DataSourceHierarchy(datasource: String, dataentity:String, datafield:String, datatype:String)
  case class EntityHierarchy(dataentity:String, datafield:String, datatype:String)
  case class FieldHierarchy(datafield:String, datatype:String)

  case class FieldDetail(datafield:String, datatype:String)
  case class DataEntityDetail(dataentity:String, datafields:Iterable[FieldDetail])
  case class DataSourceDetail (datasource: String, dataEntities:Iterable[DataEntityDetail])

  def getConnections(): Any/*Stream[DataSourceConnection]*/ = ???

  import spray.json.DefaultJsonProtocol
  object MyJsonProtocol extends DefaultJsonProtocol {
    implicit val dataSourceConnectionFormat = jsonFormat1(DataSourceConnection)
    implicit val dataformatDataFieldDetail = jsonFormat2(FieldDetail)
    implicit val dataformatDataEntityDetail = jsonFormat2(DataEntityDetail)
    implicit val dataformatDataSourceDetail = jsonFormat2(DataSourceDetail)
  }

  import spray.json._
  import DefaultJsonProtocol._ // if you do

  import MyJsonProtocol._

  def getConnectionHierarchy(message:GetConnectionHierarchy) = {
    val collection = Datasource.mongoDatabase.getCollection("hierarchy")

  }

  def addConnectionHierarchy() = {
    val collection = Datasource.mongoDatabase.getCollection("hierarchy")
    val json = DataSourceDetail("sf_demo", List(DataEntityDetail("transaction", List()))).toJson
    //val json = DataSourceConnection("helo").toJson
    System.out.println("json=" + json)
    val doc = Document.parse(json.toString())
    collection.insertOne(doc)
  }

}
