package com.speful.spark.utils

import com.speful.spark.base._
import org.apache.spark.{SparkContext => Sc}


trait SimpleCore extends CoreEnv {

  final lazy val sc = Sc getOrCreate sparkConf
  sc setCheckpointDir(if(isWindows) "E:\\test\\spark\\checkpoint" else "/spark/checkpoint")

  implicit final class And[T](obj : T){
    def >>(f : T => Unit) = {
      f(obj);obj
    }
  }
}