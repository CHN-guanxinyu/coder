package com.speful.spark.demo

import java.io.File

import com.speful.spark.utils.{GraphXUtils, SimpleSpark}
import org.apache.spark.graphx.util.{GraphGenerators => GraphGen}

object GenGraphAndSave extends App with SimpleSpark{
  val path = args(0)

  val f = new File(path)
  if( ! f.exists ) f.mkdirs

  Map(

    "gridGraph" -> GraphGen.gridGraph( sc , 4 , 4),

    "starGraph" -> GraphGen.starGraph( sc , 7 ),

    "rmatGraph" -> GraphGen.rmatGraph( sc , 32 , 60),

    "logNormalGraph" -> GraphGen.logNormalGraph(sc , 15)

  ).foreach{ case(name , graph)=>
    GraphXUtils.saveGraphTo(
      s"$path/$name.gexf",
      graph
    )
  }
}
