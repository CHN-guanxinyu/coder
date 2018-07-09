package com.speful.spark.demo

import com.speful.spark.utils.SimpleSpark
import it.nerdammer.spark.hbase._

object HbaseReader extends App with SimpleSpark {

  override def sparkConfOpts: Map[String, String] = super.sparkConfOpts ++ Map(
    "spark.hbase.host" -> "master"
  )

  val hbaseRDD = sc.
    hbaseTable[(Option[Long] , Option[String], Option[String])]("suspects_base_bcps").
    select("folder_file","values_").
    inColumnFamily("info")

  hbaseRDD take 1 foreach println
}

object HbaseWriter extends App with SimpleSpark{

  val rdd = sc.makeRDD(1 to 100).
    map(i => (i, i+1, "Hello World"))

  rdd.toHBaseTable("mytable").
    toColumns("column1", "column2").
    inColumnFamily("mycf").
    save()

  override def sparkConfOpts = super.sparkConfOpts ++ Map(
    "spark.hbase.host" -> "master"
  )
}
