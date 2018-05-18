package com.speful.spark.utils

import com.speful.spark.base.Defaults._
import com.speful.spark.base._
import org.apache.spark.{SparkConf => Sconf, SparkContext => Sc}


object SimpleSpark extends CoreEnv {

  def context(
              master : String = "",
              appName: String = appName,
              opts: Map[String, String] = Map.empty
             ) = Sc.getOrCreate( conf( appName, opts , master ) )


  def conf(
           appName: String,
           opts: Map[String, String],
           master : String
         )={
    val cfg = new Sconf().
      setAppName(appName).
      set("spark.scheduler.mode", schedulerMode).
      set("spark.serializer", serializer).
      setAll(opts)

    if("" equals master) cfg else{
      cfg setMaster master
      cfg
    }
  }




}