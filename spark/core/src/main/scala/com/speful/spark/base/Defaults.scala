package com.speful.spark.base

/**
  * 定义基本的默认参数、默认配置等信息
  */
object Defaults {

  //spark config
  val appName = "default app"
  val serializer = classOf[org.apache.spark.serializer.KryoSerializer].getName


  //system properties
  object systemProperties {
    val taskMaxFailures = 50
    val akkaTimeout = 300
    val networkTimeout = 300
  }

}


trait CoreEnv {

  //system env
  import Defaults.systemProperties._

  System.setProperty("spark.task.maxFailures", taskMaxFailures)
  System.setProperty("spark.network.timeout", networkTimeout)

  implicit def _2str: Any => String = _ toString


}
