package com.speful.demo

import com.speful.spark.utils.SimpleSpark
import org.apache.spark.streaming.Seconds

object Window extends App with SimpleSpark{
  ssc.socketTextStream("localhost" , 9999)
    .flatMap(_ split " ")
    .map(_ -> 1)
    .window( Seconds(2000) )
    .reduceByKey( _ + _ )
    .print

  ssc.start()
  ssc.awaitTermination()
}
