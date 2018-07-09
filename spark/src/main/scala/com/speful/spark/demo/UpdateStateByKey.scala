package com.speful.spark.demo

import com.speful.spark.utils.SimpleSpark

object UpdateStateByKey extends App with SimpleSpark{

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
