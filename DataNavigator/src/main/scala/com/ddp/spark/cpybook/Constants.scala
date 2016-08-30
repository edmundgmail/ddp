package com.ddp.spark.cpybook

/**
  * Created by cloudera on 8/28/16.
  */
object Constants {
  val CopybookName = "copyboook.name"
  val CopybookHdfsPath = "copyboook.hdfs.path"
  val DataFileHdfsPath = "datafile.hdfs.path"
  val CopybookSplitOpiton = "copybook.split.option"

  object CopybookSplitOptionValues extends Enumeration {
    val SplitNone = Value(0)
    val SplitRedefine = Value(1)
    val Split01Level= Value(2)
    val SplitHighestRepeating = Value(3)
  }

  val CopybookFileStructure = "copybook.file.structure"

  object CopybookFileStructureValues extends Enumeration{
    val FixedLength = Value(2)
  }

   val CopybookBinaryformat = "copybook.binary.format"

  object CopybookBinaryformatValues extends Enumeration {
    val FMT_MAINFRAME=Value(1)
    //val FMT_INTEL, FMT_MAINFRAME, FMT_FUJITSU , FMT_BIG_ENDIAN, FMT_OPEN_COBOL, FMT_FS2000,  FMT_OPEN_COBOL_MVS, FMT_OC_MICRO_FOCUS, FMT_OPEN_COBOL_BE,
     //FMT_FS2000_BE, FMT_OPEN_COBOL_MVS_BE, FMT_OC_MICRO_FOCUS_BE, FMT_MICRO_FOCUS, FMT_MAINFRAME_COMMA_DECIMAL,  FMT_FUJITSU_COMMA_DECIMAL = Value
  }

  val CopybookFont = "copybook.font"
  object CopybookFontValues extends Enumeration{
    val cp037 = Value("cp037")
  }
}
