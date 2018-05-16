package com.speful.spark.base

/**
  * 定义基本的默认参数、默认配置等信息
  */
object Defaults {

  //spark config
  val appName = "default app"
  val master = "local[*]"
  val schedulerMode = "FAIR"
  val serializer = classOf[org.apache.spark.serializer.KryoSerializer].getName


  //system properties
  object systemProperties {
    val coreMax = 5
    val taskMaxFailures = 8
    val akkaTimeout = 300
    val networkTimeout = 300
    //如果是windows测试环境下,需要指定此目录
    val hadoopHome = "D:/hadoop-2.5.2"
  }

}


trait CoreEnv {
  val WORKING_HOME = "E:/test/spark"

  //system env
  import Defaults.systemProperties._

  System.setProperty("spark.cores.max", coreMax)
  System.setProperty("spark.task.maxFailures", taskMaxFailures)
  System.setProperty("spark.akka.timeout", akkaTimeout)
  System.setProperty("spark.network.timeout", networkTimeout)
  System.setProperty("hadoop.home.dir", hadoopHome )

  implicit def _2str: Any => String = _ toString
}
