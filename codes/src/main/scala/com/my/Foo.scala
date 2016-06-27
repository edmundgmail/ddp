package com.my

import com.my.MustConform
import com.my.ScalaTest1

case class Foo(param:String) extends MustConform{
    def doIt () : String = {
      val x = new Bar
      "{'funcName' : 'Foo', 'num': '" + x.getIt + "','params': " +  param + "}" 
    }
}

case class Foo1 extends MustConform{
    def doIt () : String = {
      val param="Foo1"
      val x = new Bar
      "{'funcName' : 'Foo2', 'num': '" + x.getIt + "','params': " +  param + "}" 
    }
}

class Foo2 extends MustConform{
  def doIt(): String = {
         val param="Foo2"
      val y = new ScalaTest1 
      "{'funcName' : 'Foo2', 'num': '" + y.doIt + "','params': " +  param + "}" 
  }
}

case class Foo3(param:String) extends MustConform{
    def doIt () : String = {
      val x = new Bar

      "{'funcName' : 'Foo3', 'num': '" + x.getIt + "','params': " +  param + "}" 
    }
}

class Foo4 extends MustConform{
  def doIt(): String = {
         val param="Foo4"
      val y = new ScalaTest1 
      "{'funcName' : 'Foo4', 'num': '" + y.doIt + "','params': " +  param + "}" 
  }
}
