package com.speful.sparkall.example

import com.speful.sql.utils.SimpleSQL
import org.apache.log4j.Level
import org.apache.spark.sql.streaming.OutputMode


object StructuedStreaming extends App {

  SimpleSQL.logger setLevel Level.ERROR

  val spark = SimpleSQL.context("Structured Streaming")

  import spark.implicits._

  val lines = spark.readStream.
    format("socket").
    option("host" , "localhost").
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
