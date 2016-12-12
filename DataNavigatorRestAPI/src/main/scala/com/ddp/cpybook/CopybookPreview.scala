package com.ddp.cpybook

import java.io._
import java.util.Properties
import javax.tools._

import com.ddp.access.UserClassRunner
import com.ddp.jarmanager.InlineCompiler
import com.ddp.jdbc.{DataSourceConnection, FieldHierarchy}
import com.ddp.rest.CopybookSchemaRegisterParameter
import com.ddp.rest.copybookSchemaRegisterJsonProtocol._
import com.google.common.io.Files
import com.legstar.avro.cob2avro.Cob2AvroGenericConverter
import com.legstar.avro.generator.Cob2AvroGenerator
import com.legstar.base.`type`.composite.CobolComplexType
import com.legstar.base.converter.FromHostResult
import com.legstar.cob2xsd.Cob2XsdConfig
import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient
import net.sf.JRecord.Common.IFieldDetail
import net.sf.JRecord.Details.LayoutDetail
import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.json4s.{DefaultFormats, Formats}
import org.xeustechnologies.jcl.{JarClassLoader, JclObjectFactory}
import spray.json.{DefaultJsonProtocol, JsArray, JsNumber, JsObject, JsString, JsValue, RootJsonFormat}

import scala.collection.AbstractIterator
import scala.collection.JavaConversions._


/**
  * Created by cloudera on 11/25/16.
  */


case class CopybookPreview(jclFactory: JclObjectFactory, jcl : JarClassLoader, param: CopybookSchemaRegisterParameter, copybook:String, datafiles: Map[String, Array[Byte]]) extends UserClassRunner {
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



  override def run() : LayoutDetail = {
    val externalRecord = CopybookHelper.getExternalRecordGivenCopybook(param.cpyBookName, copybook, "Split01Level", "FMT_MAINFRAME", "cp037")

    externalRecord.asLayoutDetail
      //.foreach(field=>System.out.print("fieldname=" + field.getLookupName + ", type=" + field.getType))
      //System.out.print("fieldname=" + field.getCobolName + ", type=" + field.getType)

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
