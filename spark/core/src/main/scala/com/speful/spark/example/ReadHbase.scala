package com.speful.spark.example

import com.speful.spark.utils.SimpleSpark

object ReadHbase extends App{
  val sc = SimpleSpark.context("Hbase Reader")
  val conf= HBaseConfiguration.create()
  val hbaseRdd = sc.newAPIHadoopRDD(
    conf,
    classOf[TableInputFormat],
    classOf[org.apache.hadoop.hbase.io.ImmutableBytesWritable],
    classOf[org.apache.hadoop.hbase.client.Result])
}
