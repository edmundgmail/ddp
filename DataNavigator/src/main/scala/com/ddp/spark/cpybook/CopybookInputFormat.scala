package com.ddp.spark.cpybook

import java.io.{BufferedInputStream, IOException}

import net.sf.JRecord.External.Def.ExternalField
import net.sf.JRecord.External.{CobolCopybookLoader, CopybookLoader}
import net.sf.JRecord.Numeric.Convert
import org.apache.hadoop.conf
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.{InputSplit, RecordReader, TaskAttemptContext}
import net.sf.JRecord.External.ExternalRecord

import scala.util.Try

/**
  * Created by cloudera on 8/28/16.
  */

class CopybookInputFormat extends FileInputFormat[LongWritable, Map[String, String]]{

  @throws[IOException]
  @throws[InterruptedException]
  override def createRecordReader (split: InputSplit, context: TaskAttemptContext): RecordReader[LongWritable, Map[String, String]] = {
    // TODO Auto-generated method stub
    return new CopybookRecordReader
  }
}
