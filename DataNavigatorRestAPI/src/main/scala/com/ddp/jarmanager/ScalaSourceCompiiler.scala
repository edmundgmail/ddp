package com.ddp.jarmanager

import java.io.BufferedInputStream

import com.twitter.util.Eval
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.xeustechnologies.jcl.{JarClassLoader, JclObjectFactory}
import java.io.File
/**
  * Created by cloudera on 9/3/16.
  */
case class ScalaSourceParameter(hdfsPaths: String)

case class ScalaSourceCompiiler ( jclFactory : JclObjectFactory, jcl: JarClassLoader, sourceFiles: ScalaSourceParameter) {
  import java.io.File
  val eval = new Eval(Some(new File("")))
  val conf = new Configuration
  conf.set("fs.defaultFS", "hdfs://localhost:9000/")
  val pathArray = sourceFiles.hdfsPaths.split(":")
  val fs = FileSystem.get (conf)
  for(p<-pathArray){
    val inputStream = new BufferedInputStream (fs.open (new Path( p) ) )
    jcl.add(inputStream)
  }


}
