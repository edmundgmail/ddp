package com.ddp.access

/**
  * Created by eguo on 12/13/16.
  */


import com.twitter.util.Eval
import org.xeustechnologies.jcl.{JarClassLoader, JclObjectFactory}



trait BaseGenericRecord { def get(f:String) : Any }
//case class MyGenericRecord(s:String) extends BaseGenericRecord

object GenericRecordFactory {

  val eval = new Eval()

  val jclFactory : JclObjectFactory = JclObjectFactory.getInstance()
  val jcl: JarClassLoader = new JarClassLoader()

  def make( s: String): BaseGenericRecord = {
    eval.compile("import com.ddp.access.BaseGenericRecord;" + s)
    eval.applyProcessed[BaseGenericRecord]("MyGenericRecord(\"thisistest\")",false)
  }


  def main(args:Array[String]): Unit ={
      val x = GenericRecordFactory.make("case class MyGenericRecord(s:String) extends BaseGenericRecord { override def get(f:String) : Any = null} ")
      System.out.println("x="+x.getClass)
  }
}
