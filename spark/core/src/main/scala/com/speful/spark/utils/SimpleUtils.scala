package com.speful.spark.utils

import com.speful.spark.base._
import org.apache.spark.{SparkContext => Sc}


trait SimpleCore extends CoreEnv {

  lazy val sc = Sc getOrCreate sparkConf

  implicit class And[T](obj : T){
    def >>(f : T => Unit) = {
      f(obj);obj
    }
  }
}