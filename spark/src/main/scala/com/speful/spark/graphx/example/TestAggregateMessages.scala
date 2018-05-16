package com.speful.spark.graphx.example

import com.speful.spark.utils.SimpleSpark
import org.apache.spark.graphx.{Edge, Graph}

object TestAggregateMessages extends App {
  val sc = SimpleSpark.context()

  val vertices = sc.makeRDD(
    Array(
      (1 , "Ann"),
      (2 , "Bill"),
      (3 , "Charles"),
      (4 , "Diane"),
      (5 , "Went to gym this morning!")
    ).map{ case ( k , v )=> (k toLong , v) }
  )

  val edges = sc.makeRDD(
    Array(
      (1 , 2 , "is-friends-with"),
      (2 , 3 , "is-friends-with"),
      (3 , 4 , "is-friends-with"),
      (4 , 5 , "Likes-status"),
      (3 , 5 , "Wrote-status")
    )
    .map{ case( s , d , a ) => Edge( s toLong , d toLong , a) }
  )

  val graph = Graph( vertices , edges )

  graph.aggregateMessages[Int]( _ sendToSrc 1 , _ + _ )
    .rightOuterJoin(graph.vertices)
    .map(_._2.swap)
    .collect
    .foreach(println)
}
