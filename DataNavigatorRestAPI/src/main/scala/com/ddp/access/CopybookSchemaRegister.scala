package com.ddp.access

import java.io._
import java.net.InetAddress
import java.util.{Arrays, Properties}
import javax.tools._

import com.ddp.cpybook.Constants
import com.ddp.jarmanager.{CreateJarFile, InlineCompiler}
import com.ddp.rest.{CopybookIngestionParameter, CopybookSchemaRegisterParameter}
import com.ddp.rest.WorkerActor.Ok
import com.ddp.user.DclvrpwSAccts
import com.google.common.io.Files
import com.typesafe.config.Config
import org.apache.hadoop
import org.apache.spark.sql.SparkSession
import com.legstar.avro.generator.Cob2AvroGenerator
import com.legstar.cob2xsd.Cob2XsdConfig
import com.sun.jersey.server.impl.model.parameter.multivalued.StringReaderProviders
import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient
import org.apache.avro.Schema
import org.apache.avro.file.{DataFileReader, DataFileStream}
import org.apache.avro.generic.GenericRecord
import org.apache.avro.io.{Decoder, DecoderFactory}
import org.apache.avro.specific.{SpecificDatumReader, SpecificRecord}
import org.apache.avro.generic._
import org.apache.commons.io.input.CharSequenceReader
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.xeustechnologies.jcl.{JarClassLoader, JclObjectFactory}

import scala.collection.JavaConverters._
import com.ddp.user._
import com.legstar.avro.cob2avro.Cob2AvroGenericConverter
import com.legstar.base.`type`.composite.CobolComplexType
import com.legstar.base.converter.FromHostResult

import scala.collection.AbstractIterator
/**
  * Created by cloudera on 11/25/16.
  */
case class CopybookSchemaRegister  (jclFactory: JclObjectFactory, jcl : JarClassLoader, param: CopybookSchemaRegisterParameter, copybook:String, datafiles: Map[String, Array[Byte]]) extends UserClassRunner {
  val diagnostics: DiagnosticCollector[JavaFileObject] = new DiagnosticCollector[JavaFileObject]
  val compiler: JavaCompiler = ToolProvider.getSystemJavaCompiler
  val fileManager: StandardJavaFileManager = compiler.getStandardFileManager(diagnostics, null, null)

  val pkgPrefix = "com.ddp.user"


  val producer = {
    val props = new Properties()
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[io.confluent.kafka.serializers.KafkaAvroSerializer])
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[io.confluent.kafka.serializers.KafkaAvroSerializer])
    props.put("schema.registry.url", "http://localhost:8081")
    props.put("bootstrap.servers", "localhost:9092")
    new KafkaProducer[String, GenericRecord](props)
  }



  override def run() : Any = {
      val gen : Cob2AvroGenerator = new Cob2AvroGenerator(Cob2XsdConfig.getDefaultConfigProps)
      val file = Files.createTempDir()
      //file.deleteOnExit()

      System.out.println("copybook=" + copybook)
    try{
      gen.generate(new StringReader(copybook), file,  pkgPrefix, param.cpyBookName, null)
    }
    catch{
      case e: Exception=>
    }

     System.out.println("now start dumping files, file path=" + file.getAbsolutePath)
      val files = listAvroFile(file)
      System.out.println("")
      val schema = registerAvro (listAvroFile(file)(0))

      System.out.println("schemaname=" + schema.getName)


      InlineCompiler.compile(jclFactory, jcl, "","",recursiveListFiles(file).filter(_.isFile).filter(_.getName.startsWith("Cobol")).filter(_.getName.endsWith(".java")).asJava)
    System.out.println("path=" + file.getPath)
      jcl.add(file.getPath + "/java")
      val clazz = jclFactory.create(jcl, pkgPrefix + ".Cobol" + schema.getName).asInstanceOf[CobolComplexType]
      val converter: Cob2AvroGenericConverter = new Cob2AvroGenericConverter.Builder().cobolComplexType(clazz).
      schema(schema).build()

      datafiles.foreach{case (_,v) => sendFileToKafka(converter, v)}
      }


  private def recursiveListFiles(f: File): List[File] = {
    val these = f.listFiles.toList
    these ++ these.filter(_.isDirectory).flatMap(recursiveListFiles)
  }

  def toIter (converter: Cob2AvroGenericConverter, bytes: Array[Byte]) : Iterator[GenericRecord] = new AbstractIterator[GenericRecord] {
    var index = 0
    def hasNext = index < bytes.length // the twitter stream has no end

    def next() = {
      val result: FromHostResult[GenericRecord] = converter.convert(bytes, index, bytes.length)
      index+=result.getBytesProcessed
      result.getValue
    }
  };

  private def sendFileToKafka(converter: Cob2AvroGenericConverter, bytes: Array[Byte]): Unit ={
      toIter(converter, bytes).map(new ProducerRecord[String, GenericRecord]("topic123", "t", _)).foreach(producer.send)
  }

  private def registerAvro(file: File): Schema ={
    System.out.println("now registering")
    val client = new CachedSchemaRegistryClient("http://localhost:8081", 1000)
    val schema: Schema = new Schema.Parser().parse(file)

    client.register("topic123", schema)
    return schema
  }

  private def listAvroFile(file: File): Array[File] = {
    file.listFiles.filter(f=>f.isDirectory && f.getName.equals("avsc")).flatMap(_.listFiles)
  }

  }
