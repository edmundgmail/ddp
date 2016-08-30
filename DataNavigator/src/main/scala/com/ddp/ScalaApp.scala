package com.ddp

import scala.collection.JavaConversions._
import org.apache.spark._
import org.apache.spark.SparkConf
import org.apache.hadoop.io._
import org.apache.hadoop.mapreduce.{InputFormat => NewInputFormat, Job => NewHadoopJob}
import com.twitter.elephantbird.mapreduce.input.LzoJsonInputFormat
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.hdfs.DistributedFileSystem
import org.apache.spark.sql._
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.types.{LongType, StringType, StructField, StructType}
import com.cloudera.sa.copybook.mapreduce.CopybookInputFormat
import org.apache.hadoop
import org.apache.spark.api.java.function.Function



object ScalaApp extends App{

  override  def main(args: Array[String]) {
    import org.apache.spark.sql.SQLContext
    val sc: SparkConf = new SparkConf().setAppName("LoadJsonWithElephantBird").setMaster("local[2]")
    val jsc: SparkContext = new SparkContext(sc)
    val sqlContext = new SQLContext(jsc)
    import sqlContext._
    import spark.cpybook._
    val conf = new hadoop.conf.Configuration


    conf.set("fs.defaultFS", "hdfs://localhost:8020/");

    conf.set(Constants.CopybookName, "RPWACT")
    conf.set(Constants.CopybookHdfsPath, "/user/root/LRPWSACT.cpy")
    conf.set(Constants.CopybookFileStructure, Constants.CopybookFileStructureValues.FixedLength.toString)
    conf.set(Constants.CopybookBinaryformat, Constants.CopybookBinaryformatValues.FMT_MAINFRAME.toString)
    conf.set(Constants.CopybookSplitOpiton, Constants.CopybookSplitOptionValues.SplitNone.toString)
    conf.set(Constants.DataFileHdfsPath, "/user/root/RPWACT.FIXED.END")
    conf.set(Constants.CopybookFont, Constants.CopybookFontValues.cp037.toString)

    val trips = sqlContext.cbFile(conf)

    trips.schema.fields.foreach(println)
    trips.show(1)
    trips.registerTempTable("trips")
    trips.show(10)

    }
}