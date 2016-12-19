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
import net.sf.JRecord.Common.{BasicFileSchema, IFieldDetail}
import net.sf.JRecord.Details.{AbstractLine, LayoutDetail}
import net.sf.JRecord.External.ExternalRecord
import net.sf.JRecord.IO.{AbstractLineReader, LineIOProvider}
import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.codehaus.janino.Java
import org.json4s.{DefaultFormats, Formats}
import org.xeustechnologies.jcl.{JarClassLoader, JclObjectFactory}
import spray.json.{DefaultJsonProtocol, JsArray, JsNumber, JsObject, JsString, JsValue, RootJsonFormat}

import scala.collection.AbstractIterator
import scala.collection.JavaConversions._
import scala.util.Try


/**
  * Created by cloudera on 11/25/16.
  */

import scala.reflect.runtime.universe._

import net.sf.JRecord.Types.TypeManager



case class CopybookPreview(param: CopybookSchemaRegisterParameter, copybook:String) extends UserClassRunner {

  var externalRecord : ExternalRecord = null
  var lr: AbstractLineReader = null
  val tm = TypeManager.getSystemTypeManager()


  val producer = {
    val props = new Properties()
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[io.confluent.kafka.serializers.KafkaAvroSerializer])
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[io.confluent.kafka.serializers.KafkaAvroSerializer])
    props.put("schema.registry.url", "http://localhost:8081")
    props.put("bootstrap.servers", "localhost:9092")
    new KafkaProducer[String, GenericRecord](props)
  }

  def getType(t:Int) : Type =
    if(tm.getType(t).isNumeric) {
      t match {
        case 4 | 15 => typeOf[String]
        case _ => typeOf[String]
      }
    }else if(tm.getType(t).isBinary) {
        typeOf[Array[Byte]]
    }
    else {
      typeOf[String]
    }

  override def run() : LayoutDetail = {
    externalRecord = CopybookHelper.getExternalRecordGivenCopybook(param.cpyBookName, copybook, param.copybookSplitLevel,param.copybookBinaryFormat, param.copybookFileStructure,  param.copybookFont)

    externalRecord.asLayoutDetail
 }

  def load(datafiles : Map[String, Array[Byte]], types: Array[String]) = {

    val overrideTypes = types.map(_=>typeOf[String])

    val r = LineIOProvider.getInstance.getLineReader(externalRecord.asLayoutDetail)
    val i = datafiles.values.map(f=>ProcessFile(r,f, overrideTypes)).foldLeft(Iterator[Any]())(_ ++ _)

    "[{'a':1},{'a':2}]"
  }



  private def ProcessFile(abstractLineReader: AbstractLineReader, bytes: Array[Byte], overrideTypes : Array[Type]): Iterator[Any]  = {
    abstractLineReader.open(new ByteArrayInputStream(bytes), externalRecord.asLayoutDetail())
    toIter(abstractLineReader,bytes.length, overrideTypes)
  }


    def toIter (abstractLineReader: AbstractLineReader,bytesLength: Integer, overrideTypes: Array[Type]) : Iterator[Any] = new AbstractIterator[Any] {
      val param = externalRecord.asLayoutDetail().getFieldNameMap.values

      var index = 0
      def hasNext = index < bytesLength

      def next() = {
        val abstractLine = abstractLineReader.read()
        index+=abstractLine.getData.length
        val p= param.map(f=>abstractLine.getFieldValue(f)).toList
        System.out.println("value=" + p)
      }
    }

  }
