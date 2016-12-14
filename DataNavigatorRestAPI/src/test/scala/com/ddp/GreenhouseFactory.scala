package com.ddp

/**
  * Created by eguo on 12/13/16.
  */
package customizable

abstract class Plant

case class Rose() extends Plant

abstract class Greenhouse {
  def getPlant(): Plant
}


case class GreenhouseFactory(fileContents: String) {
  import reflect.runtime.currentMirror
  import tools.reflect.ToolBox
  val toolbox = currentMirror.mkToolBox()
  import toolbox.u._

  val tree = toolbox.parse("import customizable._; " + fileContents)
  val compiledCode = toolbox.compile(tree)

  def make(): Greenhouse = compiledCode().asInstanceOf[Greenhouse]
}

object Main {
  def main(args: Array[String]) {
    val greenhouseFactory = GreenhouseFactory("new Greenhouse {  override def getPlant() = new Rose()\n}")
    val greenhouse = greenhouseFactory.make()
    val p = greenhouse.getPlant()

    println(p)
  }
}