package com.ddp.access

import com.ddp.cpybook.Constants
import org.apache.hadoop
import org.apache.spark.sql.{SQLContext, SparkSession}
import com.ddp.cpybook._
import com.ddp.rest.CopybookIngestionParameter
import com.ddp.rest.WorkerActor.Ok
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by cloudera on 9/3/16.
  */
  case class CopybookIngestion (config: hadoop.conf.Configuration, sqlContext : SparkSession, param: CopybookIngestionParameter) extends UserClassRunner{

    override def run() : Any = {
      val conf = new hadoop.conf.Configuration(config)

      conf.set(Constants.CopybookName, param.cpyBookName)
      conf.set(Constants.CopybookHdfsPath, param.cpyBookHdfsPath  )
      conf.set(Constants.CopybookFileStructure, param.fileStructure)
      conf.set(Constants.CopybookBinaryformat, param.binaryFormat)
      conf.set(Constants.CopybookSplitOpiton, param.splitOptoin)
      conf.set(Constants.DataFileHdfsPath, param.dataFileHdfsPath)
      conf.set(Constants.CopybookFont, param.cpybookFont)
      val tempTable = param.cpyBookName + "_temp_1"

      Future{
        val trips = sqlContext.cbFile(conf)
        trips.createOrReplaceTempView(tempTable)
        trips.cache()
      }

      Ok("Table name = " + tempTable + ", appid =" + sqlContext.sparkContext.applicationId)


      //sqlContext.sql("select * from " + tempTable)

       //trips.createOrReplaceTempView(tempTable)
      //hc.sql("select * from " + tempTable)
      //hc.sql("create table " + param.cpyBookName + " as select * from " + tempTable )
      //sqlContext.sql("select * from " + tempTable)
      //sqlContext.sql("create table " + param.cpyBookName + " as select * from " + tempTable )
      //System.out.println("done create table")

    }
}
