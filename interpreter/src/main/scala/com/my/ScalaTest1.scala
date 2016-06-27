package com.my
import com.my.MustConform
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

case class Record(k: Int, v: String)

  /** Computes an approximation to pi */
class ScalaTest1 {
    def doIt() : String =  {
 val conf = new SparkConf().setAppName("Simple Application").setMaster("local[2]")
   val sc = new SparkContext(conf)
   val sqlContext = new org.apache.spark.sql.SQLContext(sc)
	val df=sqlContext.read.json("/tmp/people.json")
  df.rdd.toString()
    }
  }
