package com.speful.streaming.example

import com.speful.streaming.utils.SimpleStreaming


object TestUpdateStateByKey extends App with SimpleStreaming {

  ssc.checkpoint("E:/spark/checkpoint/")

  ssc.socketTextStream("localhost",9999)
    .flatMap( _ split " " )
    .map( _ -> 1 )
    .updateStateByKey( (curVal : Seq[Int] , preVal : Option[Int])=>
      Some( curVal.sum + preVal.getOrElse(0) )
    )
    .print

  ssc.start()
  ssc.awaitTermination()


}
