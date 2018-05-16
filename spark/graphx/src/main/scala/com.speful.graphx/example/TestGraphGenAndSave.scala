package com.speful.graphx.example

import java.io.File

import com.speful.spark.utils.{SimpleGraphX, SimpleSpark}
import org.apache.spark.graphx.util.{GraphGenerators => GraphGen}

object TestGraphGenAndSave extends App{

  val sc = SimpleSpark.context()

  val path = SimpleGraphX.HOME

  val f = new File(path)
  if( ! f.exists ) f.mkdirs

  Map(

    "gridGraph" -> GraphGen.gridGraph( sc , 4 , 4),

    "starGraph" -> GraphGen.starGraph( sc , 7 ),

    "rmatGraph" -> GraphGen.rmatGraph( sc , 32 , 60),

    "logNormalGraph" -> GraphGen.logNormalGraph(sc , 15)

  ).foreach{ case(name , graph)=>
      SimpleGraphX.saveGraphTo(
        s"$path/$name.gexf",
        graph
      )
  }



}
