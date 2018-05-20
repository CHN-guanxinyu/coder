package com.speful.sql.utils

import java.util.Properties

import com.speful.spark.base.Defaults._
import com.speful.spark.utils.SimpleSpark
import com.speful.sql.base.SqlEnv
import org.apache.spark.sql.{DataFrame, SparkSession => Sss}


object SimpleSQL extends SqlEnv {

  def context(
               appName: String = appName,
               opts: Map[String, String] = Map.empty ) =
    Sss.builder.config(SimpleSpark.conf(
      appName,
      opts
    )).getOrCreate

  def jdbc(
          url : String,
          table : String,
          user : String,
          passwd : String
          )(implicit spark : Sss) : DataFrame ={
    val prop = new Properties
    prop.setProperty("user" , user)
    prop.setProperty("password" , passwd)

    spark.read jdbc( url, table, prop )
  }
}
