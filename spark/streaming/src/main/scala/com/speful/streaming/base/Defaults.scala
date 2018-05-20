package com.speful.streaming.base

import com.speful.spark.base.CoreEnv

/**
  * 定义基本的默认参数、默认配置等信息
  */
object Defaults {

  //spark streaming config
  val concurrentJobs = 10
  val backPressureEnabled = true
  val dynamicAllocationEnabled = true

  val defualtSecond = 1

}

trait StreamingEnv extends CoreEnv{
  import Defaults._
  def seconds = defualtSecond
}