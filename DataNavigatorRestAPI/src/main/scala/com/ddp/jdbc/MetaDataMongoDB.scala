package com.ddp.jdbc

import java.sql.ResultSet
import java.util.function.Consumer

import com.ddp.rest.GetConnectionHierarchy
import com.ddp.rest.WorkerActor.Ok
import com.mongodb.client.{MongoCursor, MongoIterable}
import com.mongodb.util.JSON
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

  def streamFromResultSet[T](rs : MongoCursor[Document]) (func: Document=> T):Stream[T] = {
    if (rs.hasNext)
      func(rs.next) #:: streamFromResultSet(rs)(func)
    else
      Stream.empty
  }

  def getConnectionHierarchy(message:GetConnectionHierarchy) = {
    val collection = Datasource.mongoDatabase.getCollection("hierarchy")
    streamFromResultSet[String]( collection.find().iterator() ) { rs => rs.toString() }

  }

  def addConnectionHierarchy() = {
    val collection = Datasource.mongoDatabase.getCollection("hierarchy")
    val json = DataSourceDetail("sf_demo", List(DataEntityDetail("transaction", List()))).toJson
    //val json = DataSourceConnection("helo").toJson
    System.out.println("json=" + json)
    val doc = Document.parse(json.toString())
    collection.insertOne(doc)
  }

  def query() = {
    val q = Datasource.mongoDatabase.getCollection("hierarchy").find
    val s  = JSON.serialize(q)
    /*val s ="""
        [{"datasource" : "sf_demo" , "dataEntities" : [ { "dataentity" : "transaction" , "datafields" : [ ]}]}]
      """.stripMargin*/

    System.out.println("s=" +s)


    val o = s.parseJson.convertTo[DataSourceDetail]
    System.out.println("o=" +o.getClass)

  }

}
