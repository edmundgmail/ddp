package com.ddp.rest

import java.io.{File, OutputStream}
import java.nio.file.{Files, Paths}

import org.apache.log4j
import akka.actor.{Actor, ActorLogging, Props}
import akka.util.{ByteString, Timeout}
import com.ddp.access.CopybookIngestion
import org.apache.hadoop
import org.apache.spark.sql.{DataFrame, SQLContext, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}
import com.ddp.jarmanager.{JarLoader, JarParamter, ScalaSourceCompiiler, ScalaSourceParameter}
import com.ddp.rest.WorkerActor.{Error, Ok}
import com.ddp.userclass.RunUserClass
import com.typesafe.config.ConfigFactory
import org.apache.commons.io.FileUtils
import org.apache.log4j.{Appender, Logger, WriterAppender}
import org.xeustechnologies.jcl.{JarClassLoader, JclObjectFactory}
import spray.can.websocket.frame.{BinaryFrame, TextFrame}

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.io.Source
import scala.util.Success



case class WorkerInitialize(hello : String)

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
case class GetConnections()

case class GetDataSources(conn:String)

case class GetConnectionHierarchy(conn:String)

object WorkerActor {
  case class Ok(msg: String)
  case class Error(msg: String)

  val config = ConfigFactory.load()
  //val hadoopConfig = new hadoop.conf.Configuration
  //hadoopConfig.set("fs.defaultFS", config.getString("com.ddp.rest.defaultFS"))

  val sparkSession = org.apache.spark.sql.SparkSession.builder
    .master("spark://t440:7077")
      //.master("local[2]")
    .appName("my-spark-app")
    .config("spark.ui.port", "44040")
      .enableHiveSupport()
    .getOrCreate()


  val sparkSessionMap : Map[String, SparkSession] = Map(
    "my-spark-app" -> sparkSession
  )

  //setMaster("spark://quickstart.cloudera:7077")
  // Create the context and set the batch size
  //import sqlContext._
  //import com.ddp.cpybook._
  val jclFactory : JclObjectFactory = JclObjectFactory.getInstance()
  val jcl: JarClassLoader = new JarClassLoader()

}


class WorkerActor extends Actor with ActorLogging{
  import WorkerActor._
  import akka.util.Timeout
  import scala.concurrent.duration._

  implicit def actorRefFactory = context
  import ExecutionContext.Implicits.global
  
  //actorRefFactory.system.scheduler.scheduleOnce(5 second, self, WorkerInitialize("hello"))
  private var initailized = 0

  def workerInitialize() = {
      val wss = actorRefFactory.actorSelection("/user/wssserver1")

    val layout = Logger.getRootLogger.getAppender("console").getLayout
      Logger.getRootLogger.addAppender(new WriterAppender(layout, new AkkaActorOutputstream(wss)))

      wss ! PushToChildren("hello, wolrd")
}


  def receive = {
    {
      case msg: WorkerInitialize => {
        workerInitialize()
        sender ! Ok("intialized , hello = " + msg.hello)
      }

      case message : CopybookIngestionParameter => {
        workerInitialize()

        sender ! CopybookIngestion(config, sparkSession, message).run()
      }

      case loadjars : JarParamter => {
        workerInitialize()
        sender ! JarLoader(config, jclFactory, jcl, loadjars)
      }

      case message : QueryParameter => {
        workerInitialize()
          sender ! Query(sparkSession.sqlContext, message).query
      }

      case message: UserClassParameter => {
        workerInitialize()
        sender ! RunUserClass(jclFactory , jcl, sparkSession.sqlContext, message).run
      }

      case message: ScalaSourceParameter => {
        workerInitialize()
        sender ! ScalaSourceCompiiler(config, jclFactory , jcl, message).run
      }
      case message : GetConnections => {
        sender ! com.ddp.jdbc.MetaDataDB.getConnections()
      }

      case message : GetConnectionHierarchy => {
        sender ! com.ddp.jdbc.MetaDataDB.getConnectionHierarchy(message)
      }

      case message: GetDataSources => {
        sender ! com.ddp.access.SparkMetadata.getDataSourceDetails(sparkSessionMap(message.conn), message)
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
