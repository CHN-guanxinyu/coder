package com.speful.spark.graphx.example

import com.speful.spark.SimpleSpark
import org.apache.spark.graphx._

object TestPregel extends App {
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

  val g = Pregel(
    graph.mapVertices((v , d) => 0), 0 ,
    activeDirection = EdgeDirection.Out
  )(
    (id , vd , a) => math.max( vd , a ),
    et => Iterator(( et.dstId , et.srcAttr + 1 )),
    (a , b) => math.max(a , b)
  )

  g.vertices.collect.foreach(println)
}
