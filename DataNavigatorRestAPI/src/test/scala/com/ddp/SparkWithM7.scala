package com.ddp

import org.apache.spark._
import org.apache.spark.rdd.NewHadoopRDD
import org.apache.hadoop.hbase.{HBaseConfiguration, HTableDescriptor}
import org.apache.hadoop.hbase.client.HBaseAdmin
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HColumnDescriptor
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.HTable;

object SparkWithM7 {
  def main(args: Array[String]) {
    //Initiate spark context with spark master URL. You can modify the URL per your environment.

    val sc = new SparkContext("local[2]", "M7Test")


    val tableName = "m7Table"

    val conf = HBaseConfiguration.create()
    // Add local HBase conf
        conf.addResource(new Path("file:///opt/hbase-1.2.4/conf/hbase-site.xml"))
    conf.set(TableInputFormat.INPUT_TABLE, tableName)

    // create m7 table with column family
    val admin = new HBaseAdmin(conf)
    if(!admin.isTableAvailable(tableName)) {
      print("Creating M7 Table")
      val tableDesc = new HTableDescriptor(tableName)
      tableDesc.addFamily(new HColumnDescriptor("cf1"
        .getBytes()));
      admin.createTable(tableDesc)
    }else{
      print("Table already exists!!")
      val columnDesc = new HColumnDescriptor("cf1");
      admin.disableTable(Bytes.toBytes(tableName));
      admin.addColumn(tableName, columnDesc);
      admin.enableTable(Bytes.toBytes(tableName));
    }

    //put data into table
    val myTable = new HTable(conf, tableName);
    for (i <- 0 to 5) {
      //var p = new Put();
      //p = new Put(new String("row" + i).getBytes());
      //p.add("cf1".getBytes(), "column-1".getBytes(), new String(
       // "value " + i).getBytes());
      //myTable.put(p);
    }
    myTable.flushCommits();

    //create rdd
    val hBaseRDD = sc.newAPIHadoopRDD(conf, classOf[TableInputFormat],
      classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
      classOf[org.apache.hadoop.hbase.client.Result])

    //get the row count
    val count = hBaseRDD.count()
    print("HBase RDD count:"+count)
    System.exit(0)
  }
}
