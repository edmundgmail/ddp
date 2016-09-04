package com.ddp.access

import com.ddp.cpybook.Constants
import org.apache.hadoop
import org.apache.spark.sql.SQLContext
import com.ddp.cpybook._
import com.ddp.rest.CopybookIngestionParameter

/**
  * Created by cloudera on 9/3/16.
  */
  case class CopybookIngestion (sqlContext : SQLContext, param: CopybookIngestionParameter) extends TableGenerator{
    override def generate() : Unit = {
      val conf = new hadoop.conf.Configuration


      conf.set("fs.defaultFS", "hdfs://localhost:8020/")

      conf.set(Constants.CopybookName, param.cpyBookName)
      conf.set(Constants.CopybookHdfsPath, param.cpyBookHdfsPath  )
      conf.set(Constants.CopybookFileStructure, param.fileStructure)
      conf.set(Constants.CopybookBinaryformat, param.binaryFormat)
      conf.set(Constants.CopybookSplitOpiton, param.splitOptoin)
      conf.set(Constants.DataFileHdfsPath, param.dataFileHdfsPath)
      conf.set(Constants.CopybookFont, param.cpybookFont)

      val trips = sqlContext.cbFile(conf)

      trips.schema.fields.foreach(println)
      trips.show(1)
      trips.registerTempTable(param.cpyBookName)

    }
}
