package com.speful.sparkall.example

import com.speful.sql.utils.SimpleSpark
import it.nerdammer.spark.hbase._

object HbaseReader extends App with SimpleSpark {

  val hbaseRDD = sc.
    hbaseTable[(Int ,Int, String)]("mytable").
    select("column1", "column2" ).
    inColumnFamily("mycf")

  hbaseRDD.collect foreach println


}

object HbaseWriter extends App with SimpleSpark{

  val rdd = sc.makeRDD(1 to 100).
    map(i => (i, i+1, "Hello World"))

  rdd.toHBaseTable("mytable").
    toColumns("column1", "column2").
    inColumnFamily("mycf").
    save()
}
