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
  //val datafile = "/home/cloudera/workspace/ddp/data/sampleFXE.VB.RDW.v3.bin"
  //val cpybookfile = "/home/cloudera/workspace/ddp/data/ATMCFXE.cpy"
  val cpybookfile=args(0)
  val datafile=args(1)
  //val datafile = "/home/cloudera/workspace/ddp/data/sampleFXS.VB.RDW.v3.bin"
  //val cpybookfile = "/home/cloudera/workspace/ddp/data/ATMCFXS.cpy"

  //val datafile = "/home/cloudera/workspace/ddp/data/RPWACT.FIXED.END"
  //val cpybookfile = "/home/cloudera/workspace/ddp/data/LRPWSACT.cpy"

  //val datafile = "/home/cloudera/workspace/JRecord/SampleFiles/Fujitsu/FujitsuVariableWidthFile.seq"
  //val cpybookfile = "/home/cloudera/workspace/jrecord/Source/JRecord/src/net/sf/JRecord/zTest/Common/CopyBook/Cobol/RBIVCopy.cbl"


  val pkgPrefix = "com.ddp.user"
  val file = Files.createTempDir()
  //val param =  CopybookSchemaRegisterParameter("LRPWSACT")
  val cpybook = scala.io.Source.fromFile(cpybookfile).mkString
  val gen: Cob2AvroGenerator = new Cob2AvroGenerator(Cob2XsdConfig.getDefaultConfigProps)


  val byteArray = java.nio.file.Files.readAllBytes(Paths.get(datafile))

  gen.generate(new StringReader(cpybook), file, pkgPrefix, "avsc", null)

  val schema = registerAvro(listAvroFile(file)(0))
  InlineCompiler.compile(jclFactory, jcl, "", "", recursiveListFiles(file).filter(_.isFile).filter(_.getName.endsWith(".java")).asJava)
  jcl.add(file.getPath + "/java")
  val clazz = jclFactory.create(jcl, pkgPrefix + ".Cobol" + schema.getName).asInstanceOf[CobolComplexType];

  val converter: Cob2AvroGenericConverter = new Cob2AvroGenericConverter.Builder().cobolComplexType(clazz).
    schema(schema).build()

  //val result: FromHostResult[GenericRecord] = converter.convert(byteArray)

  //System.out.print(result.getValue.toString)
  toIter(converter, byteArray).foreach(System.out.println)

  def toIter (converter: Cob2AvroGenericConverter, bytes: Array[Byte]) : Iterator[GenericRecord] = new AbstractIterator[GenericRecord] {
    var index = 0
    def hasNext = index < bytes.length // the twitter stream has no end

    def next() = {
      val result: FromHostResult[GenericRecord] = converter.convert(bytes, index, bytes.length)
      index+=result.getBytesProcessed
      result.getValue
    }
  };

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
