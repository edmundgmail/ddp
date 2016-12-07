package com.ddp

/**
  * Created by cloudera on 12/1/16.
  */

import java.io.{ByteArrayInputStream, File, FileInputStream, StringReader}
import java.nio.file.{Path, Paths}

import com.ddp.access.CopybookSchemaRegister
import com.ddp.jarmanager.InlineCompiler
import com.ddp.rest.CopybookSchemaRegisterParameter
import com.google.common.io.Files
import org.apache.avro.Schema
import org.apache.avro.file.{DataFileReader, SeekableByteArrayInput}
import org.apache.avro.generic.{GenericDatumReader, GenericRecord}
import org.apache.avro.io.DecoderFactory
import org.apache.avro.mapred.FsInput
import org.xeustechnologies.jcl.{JarClassLoader, JclObjectFactory}
import com.google.common.io.Files
import com.legstar.avro.cob2avro.Cob2AvroGenericConverter
import com.legstar.avro.generator.Cob2AvroGenerator
import com.legstar.base.`type`.composite.CobolComplexType
import com.legstar.base.converter.FromHostResult
import com.legstar.cob2xsd.Cob2XsdConfig

import scala.collection.AbstractIterator
import scala.collection.JavaConverters._
object TestAvroFile extends App {
  System.out.println("hello")

  val jclFactory: JclObjectFactory = JclObjectFactory.getInstance()
  val jcl: JarClassLoader = new JarClassLoader()
  val datafile = "/home/eguo/workspace/ddp/data/RPWACT.FIXED.END"
  val cpybookfile = "/home/eguo/workspace/ddp/data/LRPWSACT.cpy"
  //val datafile = "/home/eguo/workspace/JRecord/SampleFiles/Fujitsu/FujitsuVariableWidthFile.seq"
  //val cpybookfile = "/home/eguo/workspace/JRecord/SampleFiles/Fujitsu/RBIVCopy.cbl"


  val pkgPrefix = "com.ddp.user"
  val file = Files.createTempDir()
  //val param =  CopybookSchemaRegisterParameter("LRPWSACT")
  val cpybook = scala.io.Source.fromFile(cpybookfile).mkString
  val gen: Cob2AvroGenerator = new Cob2AvroGenerator(Cob2XsdConfig.getDefaultConfigProps)


  val byteArray = java.nio.file.Files.readAllBytes(Paths.get(datafile))

  val datafiles = Map("RPWACT.FIXED.END" -> byteArray)
  gen.generate(new StringReader(cpybook), file, pkgPrefix, "LRPWSACT", null)

  val schema = registerAvro(listAvroFile(file)(0))
  //CopybookSchemaRegister(jclFactory, jcl, param, cpybook, datafiles).run()
  InlineCompiler.compile(jclFactory, jcl, "", "", recursiveListFiles(file).filter(_.isFile).filter(_.getName.endsWith(".java")).asJava)
  jcl.add(file.getPath + "/java")
  val clazz = jclFactory.create(jcl, pkgPrefix + ".Cobol" + schema.getName).asInstanceOf[CobolComplexType];

  val converter: Cob2AvroGenericConverter = new Cob2AvroGenericConverter.Builder().cobolComplexType(clazz).
    schema(schema).build()

  val result: FromHostResult[GenericRecord] = converter.convert(byteArray)

  System.out.print(result.getValue.toString)



  private def recursiveListFiles(f: File): List[File] = {
    val these = f.listFiles.toList
    these ++ these.filter(_.isDirectory).flatMap(recursiveListFiles)
  }

  private def registerAvro(file: File): Schema ={
    System.out.println("now registering")
    //val client = new CachedSchemaRegistryClient("http://localhost:8081", 1000)
    val schema: Schema = new Schema.Parser().parse(file)

    //client.register("topic123", schema)
    return schema
  }

  private def listAvroFile(file: File): Array[File] = {
    file.listFiles.filter(f=>f.isDirectory && f.getName.equals("avsc")).flatMap(_.listFiles)
  }

}
