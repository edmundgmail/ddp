package com.ddp.jdbc

import java.sql.{Connection, DriverManager, ResultSet}

import com.ddp.rest.GetConnectionHierarchy
import com.ddp.rest.WorkerActor.Ok
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.calcite.rel.`type`.RelDataTypeField

/**
  * Created by cloudera on 10/9/16.
  */

case class DataSourceConnection(name:String)
case class DataSourceHierarchy(datasource: String, dataentity:String, datafield:String, datatype:String)
case class EntityHierarchy(dataentity:String, datafield:String, datatype:String)
case class FieldHierarchy(datafield:String, datatype:String)

case class FieldDetail(datafield:String, datatype:String)
case class DataEntityDetail(dataentity:String, datafields:Iterable[FieldDetail])
case class DataSourceDetail (datasource: String, dataEntities:Iterable[DataEntityDetail])

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

  def getConnectionHierarchy(message:GetConnectionHierarchy) : Iterable[DataSourceDetail]= {
    try {
      val statement = Datasource.mysqlConnections.getConnection.prepareCall("call proc_connectionhierarchy(?)")
      statement.setString(1, message.conn)
      statement.execute()
      val resultSet = statement.getResultSet
      val records =streamFromResultSet(resultSet){ rs =>
          DataSourceHierarchy(rs.getString("datasource_name"), rs.getString("dataentity_name"), rs.getString("datafield_name"), rs.getString("datatype"))
        }
      //val s = records.groupBy(_.datasource).mapValues(_.groupBy(_.dataentity).mapValues(_.map(e=>new FieldHierarchy(e.datafield,e.datatype))))
      val s = records.groupBy(_.datasource).map(e=>new DataSourceDetail(e._1,  e._2.groupBy(_.dataentity).map(e=>new DataEntityDetail(e._1, e._2.map(e=>new FieldDetail(e.datafield, e.datatype))))))
      s
    }
    catch
      {
        case e:Exception => e.printStackTrace()
          null
      }
  }

}
