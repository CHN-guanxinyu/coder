package com.speful.example.spark.hbase

import org.apache.spark.{SparkConf, SparkContext}
import it.nerdammer.spark.hbase._

object Reader extends App {
  val conf = new SparkConf().
    set("spark.hbase.host" , "master").
    setMaster("local[*]").
    setAppName("Hbase Reader")

  val sc = new SparkContext(conf)


  val hbaseRDD = sc.
    hbaseTable[(Int ,Int, String)]("mytable").
    select("column1", "column2" ).
    inColumnFamily("mycf")

  hbaseRDD.collect foreach println

}

object Writer extends App{
  val conf = new SparkConf().
    set("spark.hbase.host" , "master").
    setMaster("local[*]").
    setAppName("Hbase Writer")

  val sc = new SparkContext(conf)

  val rdd = sc.parallelize(1 to 100)
    .map(i => (i, i+1, "Hello"))

  rdd.toHBaseTable("mytable")
    .toColumns("column1", "column2")
    .inColumnFamily("mycf")
    .save()
}
