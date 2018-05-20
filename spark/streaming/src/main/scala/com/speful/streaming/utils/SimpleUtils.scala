package com.speful.streaming.utils

import com.speful.spark.utils.SimpleCore
import com.speful.streaming.base.StreamingEnv
import org.apache.spark.streaming.{Seconds, StreamingContext => Ssc}


trait SimpleStreaming extends StreamingEnv with SimpleCore{

  lazy val ssc =  Ssc.getActiveOrCreate( () =>
    new Ssc( sparkConf, Seconds( seconds ) )
  )

}
