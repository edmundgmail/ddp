package com.ddp.access

import com.ddp.cpybook.Constants
import org.apache.hadoop
import org.apache.spark.sql.SQLContext
import com.ddp.cpybook._

/**
  * Created by cloudera on 9/3/16.
  */
  case class CopybookIngestion (sqlContext : SQLContext, cpybookName: String,  hdfsPath : String ,
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
