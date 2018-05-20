package com.speful.spark.utils

import com.speful.spark.base._
import org.apache.spark.{SparkContext => Sc}


trait SimpleCore extends CoreEnv {

  lazy val sc = Sc getOrCreate sparkConf

}