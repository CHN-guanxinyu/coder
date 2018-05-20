package com.speful.spark.utils

import com.speful.spark.base.Defaults._
import com.speful.spark.base._
import org.apache.spark.{SparkConf => Sconf, SparkContext => Sc}


object SimpleSpark extends CoreEnv {

  def context(
              appName: String = appName,
              opts: Map[String, String] = Map.empty
             ) = Sc.getOrCreate( conf( appName, opts ) )


  def conf(
           appName: String,
           opts: Map[String, String]
         )={

    val cfg = new Sconf().
      setAppName(appName).
      set("spark.serializer", serializer).
      setAll(opts)

    //Win环境下默认local[*]
    if( isWindows ) cfg setMaster "local[*]"

    cfg

  }

  def isWindows =
    System.getProperties.
      getProperty("os.name").
      toUpperCase.
      indexOf("WINDOWS") != -1



}