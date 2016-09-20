package com.ddp.jarmanager

import java.io.{BufferedInputStream, File, FileInputStream}

import com.twitter.util.Eval
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.xeustechnologies.jcl.{JarClassLoader, JclObjectFactory}
import org.apache.commons.io.filefilter.{FalseFileFilter, TrueFileFilter}
import org.apache.commons.io.{FileUtils, IOUtils}

import scala.collection.JavaConversions.asScalaIterator


/**
  * Created by cloudera on 9/3/16.
  */
case class ScalaSourceParameter(hdfsPaths: String)

case class ScalaSourceCompiiler ( jclFactory : JclObjectFactory, jcl: JarClassLoader, sourceFiles: ScalaSourceParameter) {





  def run():Unit = {
    val folder = new File("./target/");
    FileUtils.cleanDirectory(folder); //clean out directory (this is optional -- but good know)
    FileUtils.forceDelete(folder); //delete directory
    FileUtils.forceMkdir(folder); //
    val eval = new Eval(Some(folder))
    val conf = new Configuration

    conf.set("fs.defaultFS", "hdfs://localhost:9000/")

    val pathArray = sourceFiles.hdfsPaths.split(":")
    val fs = FileSystem.get (conf)
    for(p<-pathArray){
      val inputStream = new BufferedInputStream (fs.open (new Path( p) ) )
      eval.compile(IOUtils.toString(inputStream, "UTF-8"))
      inputStream.close()
    }

     FileUtils.listFiles(folder, TrueFileFilter.INSTANCE, FalseFileFilter.INSTANCE ).iterator().foreach(
       f=>jcl.add(new FileInputStream(f))
     )

  }

}
