package com.speful.streaming.utils

import java.io.PrintWriter

import com.speful.spark.base.Defaults._
import com.speful.spark.utils.SimpleSpark
import com.speful.streaming.base.Defaults._
import com.speful.streaming.base.{Defaults, StreamingEnv}
import org.apache.spark.streaming.{Seconds, StreamingContext => Ssc}


object SimpleStreaming extends StreamingEnv {


  def context(
           appName: String = appName,
           opts: Map[String, String] = Map.empty,
           seconds: Int = 1
         ) = {

    Ssc.getActiveOrCreate( () => new Ssc(
      SimpleSpark.conf( appName, opts )
        //提升job并行度,解决串行方式下,偶然单次数据量剧增导致的短任务延时高的问题
        .set("spark.streaming.concurrentJobs" , concurrentJobs)
        //反压机制,详见博文https://www.jianshu.com/p/87e2d66d92bb
        .set("spark.streaming.backpressure.enabled" , backPressureEnabled)
        .set("spark.dynamicAllocation.enabled" , dynamicAllocationEnabled ),
      Seconds(seconds)
    ))
  }
}
