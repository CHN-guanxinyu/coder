package com.speful.demo

import com.speful.spark.utils.SimpleSpark

object WordCount extends App with SimpleSpark{

  val data = "hello world world hello hello spark hello scala"

  sc.
    makeRDD( Seq(data) ).                     //输入
    flatMap( _ split " " ).                   //转换
    map( _ -> 1 ).                            //初始映射
    reduceByKey( _ + _ ).                     //聚合累加
    sortBy(_._2 , ascending = false , 1).             //sort
    collect.foreach(println)


}
