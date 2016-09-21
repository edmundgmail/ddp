package com.ddp.jarmanager

import java.io.BufferedInputStream

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.xeustechnologies.jcl.{JarClassLoader, JclObjectFactory}

/**
  * Created by cloudera on 9/3/16.
  */

case class JarParamter(hdfsPaths:String) /*separated with ;*/

case class JarLoader ( conf: Configuration, jclFactory : JclObjectFactory, jcl: JarClassLoader, jarParamter: JarParamter) {

  val pathArray = jarParamter.hdfsPaths.split(":")
  val fs = FileSystem.get (conf)
  for(p<-pathArray){
    val inputStream = new BufferedInputStream (fs.open (new Path( p) ) )
    jcl.add(inputStream)
  }
}
