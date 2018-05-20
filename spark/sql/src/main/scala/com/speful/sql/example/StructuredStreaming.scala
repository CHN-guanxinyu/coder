package com.speful.sql.example

import com.speful.sql.utils.SimpleSpark
import org.apache.spark
import org.apache.spark.sql.streaming.OutputMode


object StructuredStreaming extends App with SimpleSpark{

  val lines = spark.readStream.
    format("socket").
    option("host" , "master").
    option("port" , 9999).load


  lines.as[String].
    flatMap( _ split " " ).
    //withWatermark("timestamp" , "30 seconds").
    groupBy("value").
    count.
    writeStream.
    outputMode(OutputMode.Update()).
    format("console").
    start.
    awaitTermination


}