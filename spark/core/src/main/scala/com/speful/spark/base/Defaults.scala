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
    val akkaTimeout = 300
    val networkTimeout = 300
  }

}


trait CoreEnv {

  /**
    * 想修改appName只需重写即可
    * 覆盖的话一定要写在最开始
    */
  def appName : String = this.getClass.getSimpleName.filter(!_.equals('$'))

  def sparkConfOpts : Map[String , String] = Map.empty

  val logger = LogManager getLogger LogManager.ROOT_LOGGER_NAME

  //system env
  import Defaults._
  import systemProperties._

  System.setProperty("spark.task.maxFailures", taskMaxFailures)
  System.setProperty("spark.network.timeout", networkTimeout)

  implicit def _2str: Any => String = _ toString

  def sparkConf={

    val cfg = new Sconf().
      setAppName(appName).
      set("spark.serializer", serializer).
      setAll(sparkConfOpts)

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
