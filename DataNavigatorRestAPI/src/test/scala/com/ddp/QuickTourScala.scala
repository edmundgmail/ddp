package com.ddp

import com.ddp.jdbc.MetaDataMongoDB
import com.ddp.rest.GetConnectionHierarchy

/**
  * Created by cloudera on 12/22/16.
  */
object QuickTourScala extends App{

  MetaDataMongoDB.getConnectionHierarchy(GetConnectionHierarchy("hello"))
  //MetaDataMongoDB.addConnectionHierarchy

}
