package com.ddp.jdbc

import java.sql.{Connection, DriverManager, ResultSet}

import com.ddp.rest.GetConnectionHierarchy
import com.ddp.rest.WorkerActor.Ok
import com.typesafe.config.{Config, ConfigFactory}

/**
  * Created by cloudera on 10/9/16.
  */

case class DataSourceConnection(name:String)
case class ConnectionHierarchy(conn:String, datasource: String, dataentity:String, datafield:String, datatype:String)

object MetaDataDB{

  def streamFromResultSet[T](rs:ResultSet)(func: ResultSet => T):Stream[T] = {
    if (rs.next())
      func(rs) #:: streamFromResultSet(rs)(func)
    else{
      rs.close()
      Stream.empty
    }
  }

  def getConnections(): Any/*Stream[DataSourceConnection]*/ = {

    try {
      val statement = Datasource.mysqlConnections.getConnection.createStatement()
      val resultSet = statement.executeQuery(
        """
        select connection_name from connections
                                             """)
      streamFromResultSet(resultSet){ rs =>
        DataSourceConnection(rs.getString("connection_name"))
      }
    }
    catch
    {
      case e:Exception => e.printStackTrace()
        null
    }

  }

  def getConnectionHierarchy(message:GetConnectionHierarchy) : Any = {
    try {
      val statement = Datasource.mysqlConnections.getConnection.prepareCall("call proc_connectionhierarchy(?)")
      statement.setString(1, message.conn)
      statement.execute()
      val resultSet = statement.getResultSet
      streamFromResultSet(resultSet){ rs =>
          ConnectionHierarchy(message.conn, rs.getString("datasource_name"), rs.getString("dataentity_name"), rs.getString("datafield_name"), rs.getString("datatype"))
        }
    }
    catch
      {
        case e:Exception => e.printStackTrace()
          null
      }
  }

}
