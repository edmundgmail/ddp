package com.ddp.jdbc

import com.mongodb.client.{MongoCollection, MongoDatabase}
import com.mongodb.{DB, MongoClient, MongoClientURI}
import com.typesafe.config.ConfigFactory
import org.apache.commons.dbcp2.BasicDataSource
import org.bson.Document

import scala.collection.immutable.IndexedSeq



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


  def mongoDatabase : MongoDatabase  = {
    val connectionString: MongoClientURI = new MongoClientURI("mongodb://localhost:27017")
    val mongoClient: MongoClient = new MongoClient(connectionString)

    val database: MongoDatabase = mongoClient.getDatabase("metadata")
    database

  }
}