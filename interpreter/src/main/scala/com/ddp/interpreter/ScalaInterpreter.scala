package com.ddp.interpreter

import com.googlecode.scalascriptengine.ScalaScriptEngine
import java.io.File
import com.my.MustConform

object ScalaInterpreter{
       def main(args: Array[String]):Unit = {
         val c = new ScalaInterpreter
         System.out.println (c.runIt("Foo", "{}"))
         }
  
}

class ScalaInterpreter {
   val sse = ScalaScriptEngine.onChangeRefresh(new File("/home/cloudera/workspace/codes/src/main/scala/"))
   

   def runIt(funcName: String, param:String) : String = {
     try{
         sse.refresh
         val m = sse.constructors[MustConform](funcName)
         
         val t = m.newInstance(param)
         t.doIt
		           
     }catch{
       case e:Exception => e.printStackTrace
       "{}"
     }
   }
   
  
    def runIt(funcName: String):String = {
     
     try{
       sse.refresh
        val m = sse.constructors[MustConform](funcName)
       
       val t = m.newInstance
        val s = t.doIt()
        System.out.println(s)
        s
     }
     catch{
       case e:Exception => e.printStackTrace
       "{}"
     }
    }
   
   
}


