package com.ddp.rest

import akka.actor.{Actor, ActorLogging}
import com.ddp.access.CopybookIngestion
import org.apache.hadoop
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}
import com.ddp.jarmanager.{JarLoader, JarParamter}
import com.ddp.userclass.UserClassRunner
import org.apache.spark.sql.hive.HiveContext
import org.xeustechnologies.jcl.{JarClassLoader, JclObjectFactory}

case class CopybookIngestionParameter(
                               cpyBookName : String,
                               cpyBookHdfsPath : String,
                               dataFileHdfsPath: String = "",
                               cpybookFont: String = "cp037",
                               fileStructure: String = "FixedLength",
                               binaryFormat: String = "FMT_MAINFRAME",
                               splitOptoin: String = "SplitNone"
                             )

case class QueryParameter(tableName:String)

case class UserClassParameter (userClassName: String)

object WorkerActor {
  case class Ok(msg: String)
  case class Error(msg: String)

  val sparkConf = new SparkConf().setAppName("ActorWordCount").setMaster("local[2]")//.setMaster("spark://quickstart.cloudera:7077")
  // Create the context and set the batch size
  val sc = new SparkContext(sparkConf)
  val sqlContext = new SQLContext(sc)
  val hc = new HiveContext(sc)
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
          sender ! CopybookIngestion(hc, sqlContext, message).generate()
      }

      case loadjars : JarParamter => {
        sender ! JarLoader(jclFactory, jcl, loadjars)
      }

      case message : QueryParameter => {
          sender ! Query(sqlContext, message).query
      }

      case message: UserClassParameter => {
        sender ! UserClassRunner(jclFactory , jcl, sqlContext, message).run
      }
      case _ => sender ! Error("Wrong param type")
    }
  }
}

case class Query(sqlContext:SQLContext, param : QueryParameter){
  def query : Unit = {


    sqlContext.sql("select * from " + param.tableName).show(10)
  }

}
