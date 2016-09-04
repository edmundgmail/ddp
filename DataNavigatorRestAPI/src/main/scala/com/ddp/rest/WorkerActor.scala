package com.ddp.rest

import akka.actor.{Actor, ActorLogging}
import com.ddp.access.CopybookIngestion
import org.apache.hadoop
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

case class IngestionParameter(
                               format: String,
                               className: String = "",
                               cpyBookName : String = "",
                               hdfsPath : String  = "",
                               cpybookFont: String = "",
                               fileStructure: String = "",
                               binaryFormat: String = "",
                               splitOptoin: String = "",
                               dataFileHdfsPath: String = ""
                             )

case class QueryParameter(tableName:String)


object WorkerActor {
  case class Ok(msg: String)
  case class Error(msg: String)

  val sparkConf = new SparkConf().setAppName("ActorWordCount").setMaster("local[2]")
  // Create the context and set the batch size
  val sc = new SparkContext(sparkConf)
  val sqlContext = new SQLContext(sc)
  import sqlContext._
  import com.ddp.cpybook._


}




class WorkerActor extends Actor with ActorLogging {
  import WorkerActor._

  def receive = {
    {
      case message : IngestionParameter => {
         message.format match  {
          case "cpybook" => sender ! CopybookIngestion(sqlContext, message.cpyBookName, message.hdfsPath, message.cpybookFont, message.fileStructure, message.binaryFormat, message.splitOptoin, message.dataFileHdfsPath).generate()
         }
      }
      case message : QueryParameter => {
          sender ! Query(sqlContext, message.tableName).query
      }
      case _ => sender ! Error("Wrong param type")
    }
  }
}

case class Query(sqlContext:SQLContext, tableName:String){
  def query : Unit = {
    sqlContext.sql("select * from " + tableName).show(10)
  }

}
