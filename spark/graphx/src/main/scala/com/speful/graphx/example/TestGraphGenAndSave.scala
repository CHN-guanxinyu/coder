package com.speful.graphx.example

import java.io.File

import com.speful.graphx.utils.SimpleGraphX
import com.speful.spark.utils.SimpleCore
import org.apache.spark.graphx.util.{GraphGenerators => GraphGen}

object TestGraphGenAndSave extends App with SimpleGraphX{


  val path = args(0)

  val f = new File(path)
  if( ! f.exists ) f.mkdirs

  Map(

    "gridGraph" -> GraphGen.gridGraph( sc , 4 , 4),

    "starGraph" -> GraphGen.starGraph( sc , 7 ),

    "rmatGraph" -> GraphGen.rmatGraph( sc , 32 , 60),

    "logNormalGraph" -> GraphGen.logNormalGraph(sc , 15)

  ).foreach{ case(name , graph)=>
      saveGraphTo(
        s"$path/$name.gexf",
        graph
      )
  }



}
