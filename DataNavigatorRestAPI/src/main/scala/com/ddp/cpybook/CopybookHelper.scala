package com.ddp.cpybook

import java.io.BufferedInputStream

import net.sf.JRecord.External.{CobolCopybookLoader, ExternalRecord}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}

import scala.concurrent.ExecutionContext
import scala.util.Try

/**
  * Created by cloudera on 8/30/16.
  */
object CopybookHelper {
  def getExternalRecord(config: Configuration) : ExternalRecord = {

    val cbName = config.get(Constants.CopybookName)
    val cblPath = config.get (Constants.CopybookHdfsPath)
    val cbSplitOptoin = Try(Constants.CopybookSplitOptionValues withName config.get(Constants.CopybookSplitOpiton)).get.id
    val cbBinaryFormat = Try(Constants.CopybookBinaryformatValues withName config.get(Constants.CopybookBinaryformat)).get.id
    val cbFont = config.get(Constants.CopybookFont)
    val fs = FileSystem.get (config)

    val inputStream = new BufferedInputStream (fs.open (new Path ( cblPath) ) )

    val copybookInt = new CobolCopybookLoader ()

    try {
      val externalRecord = copybookInt
        .loadCopyBook(inputStream, cbName, cbSplitOptoin, 0,
          cbFont, cbBinaryFormat, 0, null)
      externalRecord
    }
    catch{
      case e: Exception => {e.printStackTrace(); null}
    }

  }

}
