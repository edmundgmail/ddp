package com.ddp.access

import com.ddp.cpybook.Constants
import com.ddp.rest.{CopybookIngestionParameter, CopybookSchemaRegisterParameter}
import com.ddp.rest.WorkerActor.Ok
import com.typesafe.config.Config
import org.apache.hadoop
import org.apache.spark.sql.SparkSession

/**
  * Created by cloudera on 11/25/16.
  */
case class CopybookSchemaRegister  (config: Config, param: CopybookSchemaRegisterParameter) extends UserClassRunner {

  override def run() : Any = {
    val conf = new hadoop.conf.Configuration
  }

  }
