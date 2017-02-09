package com.ddp.userclass

import com.ddp.access.UserClassRunner
import org.apache.spark.sql.SQLContext
import org.xeustechnologies.jcl.{JarClassLoader, JclObjectFactory}

/**
  * Created by cloudera on 9/4/16.
  */

case class UserClassParameter(userClassName:String)


case class RunUserClass (jclFactory : JclObjectFactory, jcl: JarClassLoader , sqlContext: SQLContext, message: UserClassParameter){
  def run : Unit = {
      jclFactory.create(jcl, message.userClassName).asInstanceOf[UserClassRunner].run()
  }
}
