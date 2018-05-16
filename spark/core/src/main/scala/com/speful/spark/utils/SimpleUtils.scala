package com.speful.spark.utils

import com.speful.spark.base.Defaults._
import com.speful.spark.base._
import org.apache.spark.{SparkConf => Sconf, SparkContext => Sc}


object SimpleSpark extends CoreEnv {

  def context(
              appName: String = appName,
              master: String = master,
              opts: List[(String, String)] = Nil
             ) = Sc.getOrCreate( conf( appName, master, opts ) )


  def conf(
           appName: String,
           master: String,
           opts: List[(String, String)]
         )=
    new Sconf().
      setMaster(master).
      setAppName(appName).
      set("spark.scheduler.mode", schedulerMode).
      set("spark.serializer", serializer).
      setAll(opts)


}