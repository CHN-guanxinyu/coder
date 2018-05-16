package com.speful.sql.utils

import com.speful.spark.base.Defaults._
import com.speful.spark.utils.SimpleSpark
import com.speful.sql.base.SqlEnv
import org.apache.spark.sql.{SparkSession => Sss}


object SimpleSQL extends SqlEnv {

  def context( appName: String = appName,
               master: String = master,
               opts: List[(String, String)] = Nil ) =
    Sss.builder.config(SimpleSpark.conf(
      appName,
      master,
      opts
    )).getOrCreate

}
