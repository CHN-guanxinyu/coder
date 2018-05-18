package com.speful.sparkall.example

import com.speful.sql.utils.SimpleSQL
import it.nerdammer.spark.hbase._

object HbaseReader extends App {

  val spark = SimpleSQL.
    context("local[*]","Hbase Reader" )

  val sc = spark.sparkContext

  sc.hadoopConfiguration.
    set("spark.hbase.host" , "172.16.0.104")

  val hbaseRDD = sc.
    hbaseTable[(String ,String, String)]("dbn").
    select("action" , "time" , "value" ).
    inColumnFamily("INFO")

  hbaseRDD.collect foreach println


}
