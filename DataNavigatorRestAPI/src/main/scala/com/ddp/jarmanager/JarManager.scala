package com.ddp.jarmanager

import java.io.BufferedInputStream

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.xeustechnologies.jcl.{JarClassLoader, JclObjectFactory}

/**
  * Created by cloudera on 9/3/16.
  */

case class JarParamter(hdfsPaths:String) /*separated with ;*/

case class JarLoader ( jclFactory : JclObjectFactory, jcl: JarClassLoader, jarParamter: JarParamter) {
  val conf = new Configuration
  conf.set("fs.defaultFS", "hdfs://localhost:8020/")
  val pathArray = jarParamter.hdfsPaths.split(":")
  val fs = FileSystem.get (conf)
  for(p<-pathArray){
    val inputStream = new BufferedInputStream (fs.open (new Path( p) ) )
    jcl.add(inputStream)
  }
}
