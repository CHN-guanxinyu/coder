package com.speful.sql.example

import java.util.Properties

import com.speful.sql.utils.SimpleSpark
import org.apache.spark.sql.streaming.{OutputMode, Trigger}
import org.apache.spark.sql.types.{StringType => TString, StructField => SF, StructType => ST}


object StreamingIn2_0 extends App with SimpleSpark{

  val path = "E:/test/"
  val dbUrl = "jdbc:mysql://172.16.0.127:3306/bx?useUnicode=true&characterEncoding=UTF-8"

  val schema = ST(
    SF("id" , TString ) ::
    SF("name" , TString) ::
    Nil
  )

  val data = spark.readStream
    .format("json")
    .schema( schema )
    .load("E:/test/")

  val jdbcData = jdbc(dbUrl , "test_spark_streaming" , "user124" , "123")
    .join(data)

  val query = jdbcData.writeStream
    .outputMode(OutputMode.Update())
    .format("console")
    .trigger( Trigger.ProcessingTime( 1000 ) )
    .start()

  query.awaitTermination

}
