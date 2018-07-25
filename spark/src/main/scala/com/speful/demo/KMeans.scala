package com.speful.demo

import com.speful.spark.utils.SimpleSpark
import org.apache.spark.ml.clustering.KMeans

object KMeans extends App with SimpleSpark{

  val data = spark.read.
    format("libsvm").
    load( args(1) )


  data.show

  val initMode = "k-means||"

  val numClusters = 4

  val numIterations = 100

  val model = new KMeans().
    setInitMode( initMode ).
    setK( numClusters ).
    setMaxIter( numIterations ).
    setFeaturesCol("features").
    fit(data)

  val centers = model.clusterCenters

  println("centers")
  centers foreach(v => println(v.toArray.mkString(" ")))

  // 误差计算
  val WSSSE = model.computeCost(data)
  println("Within Set Sum of Squared Errors = " + WSSSE)

}

