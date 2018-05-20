package com.speful.streaming.example

import com.speful.streaming.utils.SimpleStreaming
import org.apache.spark.streaming.Seconds

object TestWindow extends App with SimpleStreaming {

  ssc.socketTextStream("localhost" , 9999)
    .flatMap(_ split " ")
    .map(_ -> 1)
    .window( Seconds(2000) )
    .reduceByKey( _ + _ )
    .print

  ssc.start()
  ssc.awaitTermination()
}
