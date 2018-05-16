package com.speful.streaming.example

import java.util.Properties


object StreamingIn2_0 extends App {
  implicit class And[T](obj:T){
    def >>(f : T => Unit) = {f(obj) ; obj}
  }

  val sqlContext = SimpleSQL.context()

  val path = "E:/test/"
  val dbUrl = "jdbc:mysql://172.16.0.127:3306/bx?useUnicode=true&characterEncoding=UTF-8"

  val schema = ST(
    SF("id" , TString ) ::
    SF("name" , TString) ::
    Nil
  )

  val data = sqlContext.readStream
    .format("json")
    .schema( schema )
    .load("E:/test/")

  val properties = new Properties() >>
    (_ setProperty("user","user124")) >>
    (_ setProperty("password","123"))

  val jdbcData = sqlContext.read.jdbc(
    dbUrl,
    "test_spark_streaming",
    properties
  ).join(data)

  val query = jdbcData.writeStream
    .outputMode(OutputMode.Update())
    .format("console")
    .trigger( Trigger.ProcessingTime( 1000 ) )
    .start()

  query.awaitTermination

}
