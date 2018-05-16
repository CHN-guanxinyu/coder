package com.speful.streaming.example

import java.text.SimpleDateFormat
import java.util.Date

import com.speful.streaming.utils.SimpleStreaming
import org.apache.spark.storage.StorageLevel

import scala.util.Random

object SparkStreaming extends App {


  val ssc = SimpleStreaming.context()


  val wordCounts = ssc.socketTextStream("localhost",9999 , StorageLevel.MEMORY_ONLY)
    .flatMap( _ split " " )
    .map( _ -> 1 )
    .reduceByKey( _ + _ )

  def now()= {
    val fmt = new SimpleDateFormat("H:m:ss")
    fmt.format( new Date )
  }

  wordCounts foreachRDD { rdd =>
    val start = now()
    val stime = System.currentTimeMillis()
    val delay = if( Random.nextInt(10) > 8 ) 3000 else 0
    print(
      s"""
         |=======================================================================
         |start            : ${start}
         |rdd            : ${rdd.id}
         |模拟处理延迟    : ${delay} ms
         |数据量         : ${rdd.count}
      """.stripMargin)

    rdd.foreach{ kc=> Thread sleep delay }

    print(
      s"""
        |处理完毕       : ${now}
        |用时           : ${System.currentTimeMillis()-stime}
        |rdd            : ${rdd.id}
        |data           : ${rdd.collect.toMap}
      """.stripMargin)
  }

  ssc start

  ssc awaitTermination

}
