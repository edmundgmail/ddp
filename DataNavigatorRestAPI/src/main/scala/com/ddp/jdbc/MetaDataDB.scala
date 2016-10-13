package com.ddp.jdbc

import java.sql.{Connection, DriverManager, ResultSet}

import com.ddp.rest.WorkerActor.Ok
import com.typesafe.config.{Config, ConfigFactory}

/**
  * Created by cloudera on 10/9/16.
  */

case class DataSourceConnection(name:String)

object MetaDataDB{

  def streamFromResultSet[T](rs:ResultSet)(func: ResultSet => T):Stream[T] = {
    if (rs.next())
      func(rs) #:: streamFromResultSet(rs)(func)
    else{
      rs.close()
      Stream.empty
    }
  }

  def getConnections(): Any/*List[DataSourceConnection]*/ = {

    try {
      val statement = Datasource.mysqlConnections.getConnection.createStatement()
      val resultSet = statement.executeQuery(
        """
        select name from connections
                                             """)
      val s = streamFromResultSet(resultSet){ rs =>
        DataSourceConnection(rs.getString("name"))
      }
      /*      while ( resultSet.next() ) {
          val name = resultSet.getString("name")
          println("name = " + name)
      }*/

      s
      /*
      Ok( """
     [
        | {'name':'conn1'},
        |{'name':'conn2'}
        | ]
      """
     )*/
    }
    catch
    {
      case e:Exception => e.printStackTrace()
        null
    }


  }

}
