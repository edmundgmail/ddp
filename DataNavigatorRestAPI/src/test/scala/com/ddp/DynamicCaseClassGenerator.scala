package com.ddp

/**
  * Created by cloudera on 12/1/16.
  */

import com.julianpeeters.caseclass.generator._
import scala.reflect.runtime.universe._

object DynamicCaseClassGenerator extends App{
        val valueMembersA: List[FieldData] = List(FieldData("i", typeOf[Int]))
        val classDataA = ClassData(ClassNamespace(Some("models")), ClassName("MyRecord_UserDefinedRefSpecA"), ClassFieldData(valueMembersA))
        val dccA = DynamicCaseClass(classDataA)
        val typeTemplateA = dccA.newInstance(1)

}
