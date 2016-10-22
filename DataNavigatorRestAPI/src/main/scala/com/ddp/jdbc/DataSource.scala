package com.ddp.jdbc

import com.typesafe.config.ConfigFactory
import org.apache.commons.dbcp2.BasicDataSource

/**
  * Created by cloudera on 10/9/16.
  */
object Datasource {
val config = ConfigFactory.load()

  def mysqlConnections: BasicDataSource ={
    val driver = config.getString("com.ddp.mysqlconnection.jdbcDriver")
    val username = config.getString("com.ddp.mysqlconnection.username")
    val password = config.getString("com.ddp.mysqlconnection.password")
    val url = config.getString("com.ddp.mysqlconnection.url")

    val connectionPool = new BasicDataSource()

    connectionPool.setDriverClassName(driver)
    connectionPool.setUrl(url)
    connectionPool.setUsername(username)
    connectionPool.setPassword(password)
    connectionPool.setInitialSize(1)

    connectionPool
  }
}