package com.speful.sql.utils

import com.speful.spark.base.Defaults._
import com.speful.spark.utils.SimpleSpark
import com.speful.sql.base.SqlEnv
import org.apache.spark.sql.{SparkSession => Sss}


object SimpleSQL extends SqlEnv {

  def context(
               appName: String = appName,
               opts: Map[String, String] = Map.empty ) =
    Sss.builder.config(SimpleSpark.conf(
      appName,
      opts
    )).getOrCreate

}
