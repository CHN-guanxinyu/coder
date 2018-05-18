package com.speful.sparkall.example

import com.speful.sql.utils.SimpleSQL
import it.nerdammer.spark.hbase._

object HbaseReader extends App {

  val spark = SimpleSQL.context(
    args(0) ,
    "Hbase Reader" ,
    Map("spark.hbase.host" -> "master")
  )

  val sc = spark.sparkContext

  val hbaseRDD = sc.
    hbaseTable[(Int ,Int, String)]("mytable").
    select("column1", "column2" ).
    inColumnFamily("mycf")

  hbaseRDD.collect foreach println


}
