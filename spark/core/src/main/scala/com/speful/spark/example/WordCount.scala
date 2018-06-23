package com.speful.spark.example

import com.speful.spark.utils.SimpleCore


object WordCount extends App with SimpleCore{

  override def sparkConfOpts: Map[String, String] = Map(
    "spark.master" -> "local"
  )

  val data = "hello world world hello hello spark hello scala"

  sc.
  makeRDD( Seq(data) ).                     //输入
  flatMap( _ split " " ).                   //转换
  map( _ -> 1 ).                            //初始映射
  reduceByKey( _ + _ ).                     //聚合累加
  map( _ swap ).                            //排序需要按key,所以一二换位
  sortByKey(false).             //sort
  map( _ swap ).                            //还原位置
  collect.foreach(println)


}
