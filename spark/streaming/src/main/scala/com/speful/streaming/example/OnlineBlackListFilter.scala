package com.speful.streaming.example

import com.speful.streaming.utils.SimpleStreaming


object OnlineBlackListFilter extends App{


  lazy val ssc = SimpleStreaming.context( seconds = 10 )

  val blackList = ssc.sparkContext.makeRDD(Array(
    ("Spy" , true),
    ("Cheater" , true)
  ))

  val stream = ssc.socketTextStream("localhost" , 9999)




  stream.filter(_.length > 0).map { adv =>
    adv.split(" ")(1) -> adv
  }
  .transform {
    _.leftOuterJoin(blackList)
      .filter(!_._2._2.getOrElse(false))
      .map(_._2._1)
  }.print


  ssc.start
  ssc.awaitTermination
}
