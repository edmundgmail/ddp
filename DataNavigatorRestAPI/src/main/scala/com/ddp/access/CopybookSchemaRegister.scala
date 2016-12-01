package com.ddp.access

import java.io._
import java.net.InetAddress
import java.util.{Arrays, Properties}
import javax.tools._

import com.ddp.cpybook.Constants
import com.ddp.rest.{CopybookIngestionParameter, CopybookSchemaRegisterParameter}
import com.ddp.rest.WorkerActor.Ok
import com.google.common.io.Files
import com.typesafe.config.Config
import org.apache.hadoop
import org.apache.spark.sql.SparkSession
import com.legstar.avro.generator.Cob2AvroGenerator
import com.legstar.cob2xsd.Cob2XsdConfig
import com.sun.jersey.server.impl.model.parameter.multivalued.StringReaderProviders
import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient
import org.apache.avro.Schema
import org.apache.avro.file.DataFileStream
import org.apache.avro.generic.GenericRecord
import org.apache.avro.specific.SpecificDatumReader
import org.apache.commons.io.input.CharSequenceReader
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig}

/**
  * Created by cloudera on 11/25/16.
  */
case class CopybookSchemaRegister  (config: Config, param: CopybookSchemaRegisterParameter, copybook:String, datafiles: Map[String, Array[Byte]]) extends UserClassRunner {

  override def run() : Any = {
      val gen : Cob2AvroGenerator = new Cob2AvroGenerator(Cob2XsdConfig.getDefaultConfigProps)
      val file = Files.createTempDir()
      //file.deleteOnExit()

      System.out.println("copybook=" + copybook)
      gen.generate(new StringReader(copybook), file,  "com.ddp.user", param.cpyBookName, null)

      System.out.println("now start dumping files, file path=" + file.getAbsolutePath)
      val files = listAvroFile(file)
      System.out.println("")
      val schema = registerAvro (listAvroFile(file)(0))
      compileJavaFile(file)
      datafiles.mapValues(f=> sendFileToKafka(schema, f))
      }

  private def compileJavaFile(file: File): Unit = {
    val diagnostics: DiagnosticCollector[JavaFileObject] = new DiagnosticCollector[JavaFileObject]
    val compiler: JavaCompiler = ToolProvider.getSystemJavaCompiler
    val fileManager: StandardJavaFileManager = compiler.getStandardFileManager(diagnostics, null, null)

    val compilationUnit: Iterable[_ <: JavaFileObject] = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(file))
  }

  private def sendFileToKafka(schema : Schema, bytes: Array[Byte] ): Unit ={
    val props = new Properties()
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[io.confluent.kafka.serializers.KafkaAvroSerializer])
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[io.confluent.kafka.serializers.KafkaAvroSerializer]);
    props.put("schema.registry.url", "http://localhost:8081");
    // Set any other properties
    val producer = new KafkaProducer(props)

    val reader = new SpecificDatumReader[CopybookSchemaRegisterParameter]
    reader.setSchema(schema)

    val fileStream = new DataFileStream[CopybookSchemaRegisterParameter](new ByteArrayInputStream(bytes), reader)
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
