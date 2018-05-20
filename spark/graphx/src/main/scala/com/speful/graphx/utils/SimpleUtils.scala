package com.speful.graphx.utils

import java.io.PrintWriter

import com.speful.graphx.base.GraphXEnv
import org.apache.spark.graphx.{Edge, Graph}

object SimpleGraphX extends GraphXEnv{

  /**
    * 将`graph`对象存储到`path`
    * @param path
    * @param graph
    * @tparam VD
    * @tparam ED
    */
  def saveGraphTo[VD , ED](path : String , graph : Graph[VD , ED]): Unit ={
    val pw = new PrintWriter(path)

    pw.write(
      toGexf(graph)
    )

    pw.close
  }


  private def toGexf[VD , ED]( graph : Graph[VD , ED]): String ={
    implicit def toStr( obj : Any ) = obj.toString

    """<?xml version="1.0" encoding="UTF-8"?>""" +
      <gexf xmlns="http://www.gexf.net/1.3draft" version="1.3">
        <graph mode="static" defaultedgetype="directed">
          <nodes>
            {
            graph.vertices.map{ case(vertexId , data) =>
                <node id={vertexId} label={data} />
            }.collect
            }
          </nodes>

          <edges>
            {
            graph.edges.map{ case Edge(srcId , dstId , attr) =>
                <edge source={srcId} target={dstId} label={attr}/>
            }.collect
            }
          </edges>
        </graph>
      </gexf>
  }
}
