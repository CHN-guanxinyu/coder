/**
  * 封装了一些spark程序常用的对象
  * 继承SimpleSpark特质之后可以直接方便的使用以下对象:
  * --sc : SparkContext
  * --spark : SparkSession
  * --ssc : StreamingContext
  *
  * 一些内置的方法:
  * --jdbc : (url , tableName , user , passwd) => DF
  * --isWindows : => Boolean
  *
  * 一些内置的隐式转换
  * --Any => String
  * --`>>`
  *
  * 对于修改spark的配置,可以override以下变量:
  * --def appName : default current className
  * --def master : default local
  * --def sparkConfOpts : default Map.empty
  * for streaming:
  * --def second : default 1
  */
package com.speful.spark.utils

import java.io.PrintWriter
import java.util.Properties

import org.apache.spark.graphx.{Edge, Graph}
import org.apache.spark.sql.{DataFrame, SparkSession => Sss}
import org.apache.spark.streaming.{Seconds, StreamingContext => Ssc}
import org.apache.spark.{SparkConf => Sconf, SparkContext => Sc}

trait SimpleSpark extends BaseEnv {

  final lazy val sc = Sc getOrCreate sparkConf

  final lazy val spark = {
    val t = Sss.builder

    val builder = if (isWindows) t else t.enableHiveSupport

    builder config sparkConf getOrCreate
  }

  lazy val ssc: Ssc = Ssc.getActiveOrCreate(() =>
    new Ssc(sparkConf, Seconds(second))
  )

  final def jdbc(
                  url: String,
                  table: String,
                  user: String,
                  passwd: String
                ): DataFrame = {

    val prop = new Properties() >>
      (_ setProperty("user", user)) >>
      (_ setProperty("password", passwd))

    spark.read jdbc(url, table, prop)
  }

  implicit final class And[T](obj: T) {
    def >>(f: T => Unit): T = {
      f(obj)
      obj
    }
  }

}


trait BaseEnv {

  //system env

  def appName: String = this.getClass.getSimpleName.filter(!_.equals('$'))

  def master: String = "local"

  def sparkConfOpts: Map[String, String] = Map.empty

  def second: Int = 1


  final implicit def _2str: Any => String = _ toString

  final def sparkConf: Sconf = {

    val cfg = new Sconf().
      setAppName(appName).
      setMaster(master).
      setAll(sparkConfOpts)

    //Win环境下默认local[*]
    if (isWindows) cfg setMaster "local[*]"

    cfg
  }

  final def isWindows: Boolean =
    System.getProperties.
      getProperty("os.name").
      toUpperCase.
      indexOf("WINDOWS") != -1


}


object GraphXUtils {
  /**
    * 将`graph`对象存储到`path`
    *
    * @param path
    * @param graph
    * @tparam VD
    * @tparam ED
    */
  def saveGraphTo[VD, ED](path: String, graph: Graph[VD, ED]): Unit = {
    val pw = new PrintWriter(path)

    pw.write(
      toGexf(graph)
    )

    pw.close()
  }


  def toGexf[VD, ED](graph: Graph[VD, ED]): String = {
    implicit def toStr(obj: Any): String = obj.toString

    """<?xml version="1.0" encoding="UTF-8"?>""" +
      <gexf xmlns="http://www.gexf.net/1.3draft" version="1.3">
        <graph mode="static" defaultedgetype="directed">
          <nodes>
            {graph.vertices.map { case (vertexId, data) =>
              <node id={vertexId} label={data}/>
          }.collect}
          </nodes>

          <edges>
            {graph.edges.map { case Edge(srcId, dstId, attr) =>
              <edge source={srcId} target={dstId} label={attr}/>
          }.collect}
          </edges>
        </graph>
      </gexf>
  }
}