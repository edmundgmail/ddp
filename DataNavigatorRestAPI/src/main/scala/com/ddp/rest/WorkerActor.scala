package com.ddp.rest

import java.io.File
import java.nio.file.{Files, Paths}

import akka.actor.{Actor, ActorLogging}
import com.ddp.access.CopybookIngestion
import org.apache.hadoop
import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}
import com.ddp.jarmanager.{JarLoader, JarParamter}
import com.ddp.rest.WorkerActor.{Error, Ok}
import com.ddp.userclass.UserClassRunner
import org.apache.commons.io.FileUtils
import org.apache.spark.sql.hive.HiveContext
import org.xeustechnologies.jcl.{JarClassLoader, JclObjectFactory}

import scala.io.Source





case class CopybookIngestionParameter(
                               cpyBookName : String,
                               cpyBookHdfsPath : String,
                               dataFileHdfsPath: String = "",
                               cpybookFont: String = "cp037",
                               fileStructure: String = "FixedLength",
                               binaryFormat: String = "FMT_MAINFRAME",
                               splitOptoin: String = "SplitNone"
                             )

case class QueryParameter(sql:String)

case class UserClassParameter (userClassName: String)

object WorkerActor {
  case class Ok(msg: String)
  case class Error(msg: String)

  val sparkSession = org.apache.spark.sql.SparkSession.builder
    //.master("spark://quickstart.cloudera:7077")
      .master("local[2]")
    .appName("my-spark-app")
    .config("spark.some.config.option", "config-value")
      .enableHiveSupport()
    .getOrCreate()

  //setMaster("spark://quickstart.cloudera:7077")
  // Create the context and set the batch size
  //import sqlContext._
  //import com.ddp.cpybook._
  val jclFactory : JclObjectFactory = JclObjectFactory.getInstance()
  val jcl: JarClassLoader = new JarClassLoader()

}




class WorkerActor extends Actor with ActorLogging {
  import WorkerActor._

  def receive = {
    {
      case message : CopybookIngestionParameter => {
          sender ! CopybookIngestion(sparkSession, message).generate()
      }

      case loadjars : JarParamter => {
        sender ! JarLoader(jclFactory, jcl, loadjars)
      }

      case message : QueryParameter => {
          sender ! Query(sparkSession.sqlContext, message).query
      }

      case message: UserClassParameter => {
        sender ! UserClassRunner(jclFactory , jcl, sparkSession.sqlContext, message).run
      }
      case _ => sender ! Error("Wrong param type")
    }
  }
}

case class Query(sqlContext:SQLContext, param : QueryParameter){
  def query : Any = {
    val path = "/tmp/" + System.currentTimeMillis + "_" + util.Random.nextInt(10000) + ".tmp"

    try {

      sqlContext.sql(param.sql).write.json(path)
    }

    if (Files.exists(Paths.get(path)) && Files.exists(Paths.get(path + "/_SUCCESS"))) {
      val f = new File(path)
      val x = f.listFiles().filter(_.getName.endsWith(".json")).flatMap(Source fromFile _ getLines() mkString)

      FileUtils.deleteQuietly(new java.io.File(path))

      System.out.println("Query x = " + x)
      Ok(x.toString)
    }
    else {
      Error("error")
    }
  }
}
