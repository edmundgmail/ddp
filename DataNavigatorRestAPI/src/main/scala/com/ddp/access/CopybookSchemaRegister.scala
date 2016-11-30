package com.ddp.access

import java.io._
import java.net.InetAddress
import java.util.Properties

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

/**
  * Created by cloudera on 11/25/16.
  */
case class CopybookSchemaRegister  (config: Config, param: CopybookSchemaRegisterParameter, copybook:String, datafiles: Map[String, Array[Byte]]) extends UserClassRunner {

  override def run() : Any = {
      val gen : Cob2AvroGenerator = new Cob2AvroGenerator(Cob2XsdConfig.getDefaultConfigProps)
      val file = Files.createTempDir()
      file.deleteOnExit()
      gen.generate(new StringReader(copybook), file,  "com.ddp.user", param.cpyBookName, null)

      System.out.println("now start dumping files, file path=" + file.getAbsolutePath)
      listAvroFile(file).map(registerAvro)

      datafiles.mapValues(sendFileToKafka)
      }

  private def sendFileToKafka( bytes: Array[Byte] ): Unit ={
    val config = new Properties();
    config.put("client.id", InetAddress.getLocalHost().getHostName());
    config.put("bootstrap.servers", "host1:9092,host2:9092");
    config.put("acks", "all");
    val producer = new KafkProducer<K, V>(config);
  }

  private def registerAvro(file: File): Unit ={
    System.out.println("now registering")
    val client = new CachedSchemaRegistryClient("http://localhost:8081", 1000)
    val schema: Schema = new Schema.Parser().parse(file)

    client.register("topic123", schema)
  }

  private def listAvroFile(file: File): Array[File] = {
    file.listFiles.filter(f=>f.isDirectory && f.getName.equals("avsc")).flatMap(_.listFiles)
  }

  }
