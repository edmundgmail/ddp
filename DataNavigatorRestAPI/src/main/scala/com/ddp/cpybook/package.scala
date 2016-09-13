package com.ddp

import org.apache.hadoop.conf.Configuration
import org.apache.spark.sql.{SQLContext, SparkSession}

package object cpybook {

  /**
   * Adds a method, `dbfFile`, to SQLContext that allows reading data stored in DBF.
   */
  implicit class DBFSQLContext(sqlContext: SparkSession) {
    def cbFile(conf: Configuration) =
      sqlContext.baseRelationToDataFrame(CPBRelation(conf)(sqlContext.sqlContext))
  }



}
