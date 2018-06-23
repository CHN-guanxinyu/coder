package com.speful.spark.base

import org.apache.logging.log4j.LogManager
import org.apache.spark.{SparkConf => Sconf}

/**
  * 定义基本的默认参数、默认配置等信息
  */
object Defaults {

  //spark config
  val serializer = classOf[org.apache.spark.serializer.KryoSerializer].getName


  //system properties
  object systemProperties {
    val taskMaxFailures = 50
    val akkaTimeout = 99999
    val networkTimeout = 99999
  }

}


trait CoreEnv {

  /**
    * 想修改appName只需重写即可
    * 覆盖的话一定要写在最开始
    */
  def appName : String = this.getClass.getSimpleName.filter(!_.equals('$'))

  def sparkConfOpts : Map[String , String]

  final val logger = LogManager getLogger LogManager.ROOT_LOGGER_NAME

  //system env
  import Defaults._
  import systemProperties._

  System.setProperty("spark.task.maxFailures", taskMaxFailures)
  System.setProperty("spark.network.timeout", networkTimeout)

  final implicit def _2str: Any => String = _ toString

  final def sparkConf={

    val cfg = new Sconf().
      setAppName(appName).
      set("spark.serializer", serializer).
      set("spark.scheduler.mode" , "FAIR").
      set("spark.memory.fraction" , 0.9).
      setAll(sparkConfOpts)

    //Win环境下默认local[*]
    if( isWindows ) cfg setMaster "local[*]"

    cfg
  }

  final def isWindows =
    System.getProperties.
      getProperty("os.name").
      toUpperCase.
      indexOf("WINDOWS") != -1


}
