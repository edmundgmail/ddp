package com.ddp.access

import com.ddp.cpybook.Constants
import org.apache.hadoop
import org.apache.spark.sql.{SQLContext, SparkSession}
import com.ddp.cpybook._
import com.ddp.rest.CopybookIngestionParameter
import org.apache.spark.sql.hive.HiveContext

/**
  * Created by cloudera on 9/3/16.
  */
  case class CopybookIngestion (sqlContext : SparkSession, param: CopybookIngestionParameter) extends TableGenerator{
    override def generate() : Unit = {
      val conf = new hadoop.conf.Configuration


      conf.set("fs.defaultFS", "hdfs://eguo-linux:8020/")

      conf.set(Constants.CopybookName, param.cpyBookName)
      conf.set(Constants.CopybookHdfsPath, param.cpyBookHdfsPath  )
      conf.set(Constants.CopybookFileStructure, param.fileStructure)
      conf.set(Constants.CopybookBinaryformat, param.binaryFormat)
      conf.set(Constants.CopybookSplitOpiton, param.splitOptoin)
      conf.set(Constants.DataFileHdfsPath, param.dataFileHdfsPath)
      conf.set(Constants.CopybookFont, param.cpybookFont)

      val trips = sqlContext.cbFile(conf)

      trips.schema.fields.foreach(println)
      trips.show(10)
      val tempTable = param.cpyBookName + "_temp_1"
      trips.createOrReplaceTempView(tempTable)

      //sqlContext.sql("select * from " + tempTable)
      sqlContext.sql("create table testdb.b(b int, c string) partition by b,c")

       //trips.createOrReplaceTempView(tempTable)
      //hc.sql("select * from " + tempTable)
      //hc.sql("create table " + param.cpyBookName + " as select * from " + tempTable )
      //sqlContext.sql("select * from " + tempTable)
      //sqlContext.sql("create table " + param.cpyBookName + " as select * from " + tempTable )
      //System.out.println("done create table")

    }
}
