package com.ddp.cpybook

import com.ddp.access.UserClassRunner
import com.ddp.rest.CopybookIngestionParameter
import com.ddp.rest.WorkerActor.Ok
import com.typesafe.config.Config
import org.apache.hadoop
import org.apache.spark.sql.SparkSession

/**
  * Created by cloudera on 9/3/16.
  */
  case class CopybookIngestion (config: Config, sqlContext : SparkSession, param: CopybookIngestionParameter) extends UserClassRunner{

    override def run() : Any = {
      val conf = new hadoop.conf.Configuration
      conf.set("fs.defaultFS", config.getString("com.ddp.rest.defaultFS"))

      conf.set(Constants.CopybookName, param.cpyBookName)
      conf.set(Constants.CopybookHdfsPath, param.cpyBookHdfsPath  )
      conf.set(Constants.CopybookFileStructure, param.fileStructure)
      conf.set(Constants.CopybookBinaryformat, param.binaryFormat)
      conf.set(Constants.CopybookSplitOpiton, param.splitOptoin)
      conf.set(Constants.DataFileHdfsPath, param.dataFileHdfsPath)
      conf.set(Constants.CopybookFont, param.cpybookFont)
      val tempTable = param.cpyBookName + "_temp_1"

      //Future {
        try {
          val trips = sqlContext.cbFile(conf)
          trips.createOrReplaceTempView(tempTable)
          trips.cache()
          }
          catch {
          case e: Throwable => e.printStackTrace()
        }
      //}
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
