package com.ddp.rest

import java.io.{File, OutputStream}
import java.nio.file.{Files, Paths}

import org.apache.log4j
import akka.actor.{Actor, ActorLogging}
import akka.util.Timeout
import com.ddp.access.CopybookIngestion
import org.apache.hadoop
import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}
import com.ddp.jarmanager.{JarLoader, JarParamter, ScalaSourceCompiiler, ScalaSourceParameter}
import com.ddp.rest.WorkerActor.{Error, Ok}
import com.ddp.userclass.RunUserClass
import com.typesafe.config.ConfigFactory
import org.apache.commons.io.FileUtils
import org.apache.log4j.{Appender, Logger}
import org.xeustechnologies.jcl.{JarClassLoader, JclObjectFactory}

import scala.concurrent.{Await, ExecutionContext, Future}
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

  val config = ConfigFactory.load()
  //val hadoopConfig = new hadoop.conf.Configuration
  //hadoopConfig.set("fs.defaultFS", config.getString("com.ddp.rest.defaultFS"))

  val sparkSession = org.apache.spark.sql.SparkSession.builder
    //.master("spark://eguo-linux:7077")
      .master("local[2]")
    .appName("my-spark-app")
    .config("spark.ui.port", "44040")
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
  import akka.util.Timeout
  import scala.concurrent.duration._

  def actorRefFactory = context

  implicit val resolveTimeout = Timeout(5 seconds)
  //val wss = actorRefFactory.actorSelection("/user/wssserver1/worker1").resolveOne(), resolveTimeout.duration).asInstanceOf[OutputStream]


  def receive = {
    {
      case message : CopybookIngestionParameter => {

        //actorRefFactory.actorSelection("/user/wssserver1/worker1") ! Push("again hello, world")
        lazy val layout = Logger.getRootLogger.getAppender("console").getLayout
        //Logger.getRootLogger.addAppender(new OutputStreamAppender(layout, wss))


        lazy val wss = Await.result(actorRefFactory.actorSelection("/user/wssserver1/worker1").resolveOne(), resolveTimeout.duration).asInstanceOf[OutputStream]

        Logger.getRootLogger.addAppender(new org.apache.log4j.WriterAppender(layout, wss))
        sender ! CopybookIngestion(config, sparkSession, message).run()
      }

      case loadjars : JarParamter => {
        sender ! JarLoader(config, jclFactory, jcl, loadjars)
      }

      case message : QueryParameter => {
          sender ! Query(sparkSession.sqlContext, message).query
      }

      case message: UserClassParameter => {
        sender ! RunUserClass(jclFactory , jcl, sparkSession.sqlContext, message).run
      }

      case message: ScalaSourceParameter => {
        sender ! ScalaSourceCompiiler(config, jclFactory , jcl, message).run
      }

      case _ => sender ! Error("Wrong param type")
    }
  }
}

import ExecutionContext.Implicits.global

case class Query(sqlContext:SQLContext, param : QueryParameter){
  def query : Any = {
    val path = "/tmp/" + System.currentTimeMillis + "_" + util.Random.nextInt(10000) + ".tmp"


    Future{
        try {
          sqlContext.sql(param.sql).write.json(path)

        }
        catch
          {
            case _ :Throwable =>
          }
    }
    Ok(path + ",appid=" +  sqlContext.sparkSession.sparkContext.applicationId)

  }
}
