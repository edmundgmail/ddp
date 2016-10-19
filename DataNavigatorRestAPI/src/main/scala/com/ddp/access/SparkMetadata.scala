package com.ddp.access

import com.ddp.rest.GetDataSources
import org.apache.spark.sql.SparkSession

/**
  * Created by cloudera on 10/18/16.
  */

case class DataSourceDetail(datasourceName: String)
object SparkMetadata {
  def getDataSourceDetails(sparkSession : SparkSession, meesage: GetDataSources) : Any = {
      val s = sparkSession.sql("show databases")
       s.toJSON.collect().foreach(println)
    val x = s.toJSON.collect().foldLeft("")(_+_)
    println("x=" + x)
    x
  }
}
