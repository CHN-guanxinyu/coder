package com.speful.spark.utils

import com.speful.spark.base.Defaults._
import com.speful.spark.base._
import org.apache.spark.{SparkConf => Sconf, SparkContext => Sc}


object SimpleSpark extends CoreEnv {

  def context(
              appName: String = appName,
              opts: List[(String, String)] = Nil
             ) = Sc.getOrCreate( conf( appName, opts ) )


  def conf(
           appName: String,
           opts: List[(String, String)]
         )=
    new Sconf().
      setAppName(appName).
      set("spark.scheduler.mode", schedulerMode).
      set("spark.serializer", serializer).
      setAll(opts)


}