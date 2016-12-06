package com.ddp

/**
  * Created by cloudera on 12/1/16.
  */

import java.io.{File, FileInputStream}
import java.nio.file.{Files, Path, Paths}

import com.ddp.access.CopybookSchemaRegister
import com.ddp.rest.CopybookSchemaRegisterParameter
import org.xeustechnologies.jcl.{JarClassLoader, JclObjectFactory}

object TestAvroFile extends App{
        System.out.println("hello")

        val jclFactory : JclObjectFactory = JclObjectFactory.getInstance()
        val jcl: JarClassLoader = new JarClassLoader()
        val datafile = "/home/cloudera/workspace/ddp/data/RPWACT.FIXED.END"


        val param =  CopybookSchemaRegisterParameter("LRPWSACT")
        val cpybook = scala.io.Source.fromFile("/home/cloudera/workspace/ddp/data/LRPWSACT.cpy").getLines.reduceLeft(_+_)

        val byteArray = Files.readAllBytes(Paths.get(datafile))

        val datafiles = Map("RPWACT.FIXED.END"->byteArray)

        //CopybookSchemaRegister(jclFactory, jcl, param, cpybook, datafiles).run()

  val input = new FsInput(new Path(, config);
  DatumReader<GenericRecord> reader = new GenericDatumReader<>();
  FileReader<GenericRecord> fileReader = DataFileReader.openReader(input, reader);

}
