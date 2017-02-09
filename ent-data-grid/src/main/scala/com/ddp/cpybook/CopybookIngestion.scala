package com.ddp.cpybook

import com.ddp.access.UserClassRunner
import org.apache.hadoop
import org.apache.spark.sql.SparkSession

/**
  * Created by cloudera on 9/3/16.
  */

case class CopybookIngestionParameter(
                               conn:String,
                               cpyBookName : String,
                               cpyBookHdfsPath : String,
                               dataFileHdfsPath: String = "",
                               cpybookFont: String = "cp037",
                               fileStructure: String = "FixedLength",
                               binaryFormat: String = "FMT_MAINFRAME",
                               splitOptoin: String = "SplitNone"
                             )

case class CopybookSchemaRegisterParameter(
  cpyBookName : String,
  copybookSplitLevel : String = "Split01Level",
  copybookFileStructure : String = "FixedLength",
  copybookBinaryFormat: String = "FMT_MAINFRAME",
  copybookFont : String = "cp037"
)


  case class CopybookIngestion (sqlContext : SparkSession, param: CopybookIngestionParameter) extends UserClassRunner{

    override def run() : Any = {
      val conf = new hadoop.conf.Configuration
      conf.set("fs.defaultFS", "com.ddp.rest.defaultFS")

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


      //sqlContext.sql("select * from " + tempTable)

       //trips.createOrReplaceTempView(tempTable)
      //hc.sql("select * from " + tempTable)
      //hc.sql("create table " + param.cpyBookName + " as select * from " + tempTable )
      //sqlContext.sql("select * from " + tempTable)
      //sqlContext.sql("create table " + param.cpyBookName + " as select * from " + tempTable )
      //System.out.println("done create table")

    }
}
