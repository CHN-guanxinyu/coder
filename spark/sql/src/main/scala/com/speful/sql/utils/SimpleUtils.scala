package com.speful.sql.utils

import java.util.Properties

import com.speful.spark.utils.SimpleCore
import com.speful.sql.base.SparkEnv
import org.apache.spark.sql.{DataFrame, SparkSession => Sss}


trait SimpleSpark extends SparkEnv with SimpleCore {

  final lazy val spark = Sss.builder config sparkConf getOrCreate

  final def jdbc(
            url: String,
            table: String,
            user: String,
            passwd: String
          ): DataFrame = {

    val prop = new Properties() >>
      (_ setProperty("user", user)) >>
      (_ setProperty("password", passwd))

    spark.read jdbc(url, table, prop)
  }
}
