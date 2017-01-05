package com.ddp

import com.ddp.jdbc.MetaDataMongoDB.DataSourceDetail
import com.ddp.jdbc.{Datasource, MetaDataMongoDB}
import com.ddp.rest.GetConnectionHierarchy
import com.mongodb.client.MongoDatabase
import com.mongodb.util.JSON

/**
  * Created by cloudera on 12/22/16.
  */
object QuickTourScala extends App{

  //System.out.println(MetaDataMongoDB.getConnectionHierarchy(GetConnectionHierarchy("hello")).mkString)
  //MetaDataMongoDB.addConnectionHierarchy

  MetaDataMongoDB.query()

}
