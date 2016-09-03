package com.ddp.rest

import akka.actor.{Actor, ActorLogging}
import com.ddp.cpybook._
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

trait TableGenerator{
  def generate() : Unit
}


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


case class CpybookIngestion (sqlContext : SQLContext, cpybookName: String,  hdfsPath : String ,
                             cpybookFont: String,
                             fileStructure: String,
                             binaryFormat: String,
                             splitOptoin: String,
                             dataFileHdfsPath: String) extends TableGenerator{
  override def generate() : Unit = {
    val conf = new hadoop.conf.Configuration


    conf.set("fs.defaultFS", "hdfs://localhost:8020/");
/*
    conf.set(Constants.CopybookName, "RPWACT")
    conf.set(Constants.CopybookHdfsPath, "/user/root/LRPWSACT.cpy")
    conf.set(Constants.CopybookFileStructure, Constants.CopybookFileStructureValues.FixedLength.toString)
    conf.set(Constants.CopybookBinaryformat, Constants.CopybookBinaryformatValues.FMT_MAINFRAME.toString)
    conf.set(Constants.CopybookSplitOpiton, Constants.CopybookSplitOptionValues.SplitNone.toString)
    conf.set(Constants.DataFileHdfsPath, "/user/root/RPWACT.FIXED.END")
    conf.set(Constants.CopybookFont, Constants.CopybookFontValues.cp037.toString)
*/
    conf.set(Constants.CopybookName, cpybookName)
    conf.set(Constants.CopybookHdfsPath, hdfsPath)
    conf.set(Constants.CopybookFileStructure, fileStructure)
    conf.set(Constants.CopybookBinaryformat, binaryFormat)
    conf.set(Constants.CopybookSplitOpiton, splitOptoin)
    conf.set(Constants.DataFileHdfsPath, dataFileHdfsPath)
    conf.set(Constants.CopybookFont, cpybookFont)

    val trips = sqlContext.cbFile(conf)

    trips.schema.fields.foreach(println)
    trips.show(1)
    trips.registerTempTable("RPWACT")

    //trips.show(10)
  }
}

class WorkerActor extends Actor with ActorLogging {
  import WorkerActor._

  def receive = {
    {
      case message : IngestionParameter => {
         message.format match  {
          case "cpybook" => sender ! CpybookIngestion(sqlContext, message.cpyBookName, message.hdfsPath, message.cpybookFont, message.fileStructure, message.binaryFormat, message.splitOptoin, message.dataFileHdfsPath).generate()
         }
      }
      case _ => sender ! Error("Wrong param type")
    }
  }
}

