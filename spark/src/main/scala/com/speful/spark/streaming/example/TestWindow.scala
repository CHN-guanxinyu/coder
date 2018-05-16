package com.speful.spark.streaming.example

import com.speful.spark.utils.SimpleStreaming
import org.apache.spark.streaming.Seconds

object TestWindow extends App{
  val ssc = SimpleStreaming.context()

  ssc.socketTextStream("localhost" , 9999)
    .flatMap(_ split " ")
    .map(_ -> 1)
    .window( Seconds(2000) )
    .reduceByKey( _ + _ )
    .print

  ssc.start()
  ssc.awaitTermination()
}
