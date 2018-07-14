package com.speful.spark.utils

import java.io.PrintWriter
import java.util.Properties

import org.apache.spark.graphx.{Edge, Graph}
import org.apache.spark.sql.{DataFrame, SparkSession => Sss}
import org.apache.spark.streaming.{Seconds, StreamingContext => Ssc}
import org.apache.spark.{SparkConf => Sconf, SparkContext => Sc}

private trait SimpleSparkCore
private trait SimpleSparkSql
private trait SimpleSparkGraphX
private trait SimpleSparkStreaming

trait SimpleSpark extends BaseEnv {

  final lazy val sc = Sc getOrCreate sparkConf

  final lazy val spark = Sss.builder.enableHiveSupport config sparkConf getOrCreate

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


/**
  * 定义基本的默认参数、默认配置等信息
  */
object Defaults {
  //spark streaming config
  val concurrentJobs = 10
  val backPressureEnabled = true
  val dynamicAllocationEnabled = true

  val second = 1

  //spark config
  val serializer: String = classOf[org.apache.spark.serializer.KryoSerializer].getName


  //system properties
  object systemProperties {
    val taskMaxFailures = 50
    val akkaTimeout = 99999
    val networkTimeout = 99999
  }

}


trait BaseEnv {

  //system env

  def appName: String = this.getClass.getSimpleName.filter(!_.equals('$'))

  def master: String = "local"

  def sparkConfOpts: Map[String, String] = Map.empty

  def second: Int = Defaults.second


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