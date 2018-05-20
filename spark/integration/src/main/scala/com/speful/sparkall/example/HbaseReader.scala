package com.speful.sparkall.example

import com.speful.sql.utils.SimpleSQL
import it.nerdammer.spark.hbase._

object HbaseReader extends App {

  val spark = SimpleSQL.context(
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

object HbaseWriter extends App{
  val spark = SimpleSQL.context(
    "Hbase Writer",
    Map("spark.hbase.host" -> "master")
  )

  val sc = spark.sparkContext

  val rdd = sc.makeRDD(1 to 100).
    map(i => (i, i+1, "Hello World"))

  rdd.toHBaseTable("mytable").
    toColumns("column1", "column2").
    inColumnFamily("mycf").
    save()
}
