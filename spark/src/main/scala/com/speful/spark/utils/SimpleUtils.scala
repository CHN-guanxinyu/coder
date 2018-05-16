package com.speful.spark.utils

import java.io.PrintWriter

import org.apache.log4j.Logger
import org.apache.spark.graphx.{Edge, Graph}
import org.apache.spark.sql.{SparkSession => Sss}
import org.apache.spark.streaming.{Seconds, StreamingContext => Ssc}
import org.apache.spark.{SparkConf => Sconf, SparkContext => Sc}

/**
  * 定义基本的默认参数、默认配置等信息
  */
object Defaults {

  //spark config
  object Spark {

    val appName = "default app"
    val master = "local[*]"
    val schedulerMode = "FAIR"
    val serializer = classOf[org.apache.spark.serializer.KryoSerializer].getName

    object Streaming {
      val concurrentJobs = 10
      val backPressureEnabled = true
      val dynamicAllocationEnabled = true

    }

    object Sql

  }

  //system properties
  object systemProperties {
    val coreMax = 5
    val taskMaxFailures = 8
    val akkaTimeout = 300
    val networkTimeout = 300
    //如果是windows测试环境下,需要指定此目录
    val hadoopHome = "D:/hadoop-2.5.2"
  }

}






/**
  * 运行时环境基本配置
  */
sealed trait BaseEnv {
  val WORKING_HOME = "E:/test/spark"

  //system env
  import Defaults.systemProperties._
  System.setProperty("spark.cores.max", coreMax)
  System.setProperty("spark.task.maxFailures", taskMaxFailures)
  System.setProperty("spark.akka.timeout", akkaTimeout)
  System.setProperty("spark.network.timeout", networkTimeout)
  System.setProperty("hadoop.home.dir", hadoopHome )

  implicit def _2str: Any => String = _ toString
}



import Defaults.Spark._

object SimpleSpark extends BaseEnv {

  def context(
              appName: String = appName,
              master: String = master,
              opts: List[(String, String)] = Nil
             ) =
    Sc.getOrCreate( conf( appName, master, opts ) )




  private[utils] def conf(
           appName: String,
           master: String,
           opts: List[(String, String)]
         )=
    new Sconf().setMaster(master).setAppName(appName)
      .set("spark.scheduler.mode", schedulerMode)
      .set("spark.serializer", serializer)
      .setAll(opts)


}

object SimpleStreaming extends BaseEnv {

  import Streaming._

  def context(
           appName: String = appName,
           master: String = master,
           opts: List[(String, String)] = Nil,
           seconds: Int = 1
         ) = {

    Ssc.getActiveOrCreate( () => new Ssc(
      SimpleSpark.conf( appName, master, opts )
        //提升job并行度,解决串行方式下,偶然单次数据量剧增导致的短任务延时高的问题
        .set("spark.streaming.concurrentJobs" , concurrentJobs)
        //反压机制,详见博文https://www.jianshu.com/p/87e2d66d92bb
        .set("spark.streaming.backpressure.enabled" , backPressureEnabled)
        .set("spark.dynamicAllocation.enabled" , dynamicAllocationEnabled ),
      Seconds(seconds)
    ))
  }
}


object SimpleSQL extends BaseEnv {

  def context(
              appName: String = appName,
              master: String = master,
              opts: List[(String, String)] = Nil
            ) = {
    Sss.builder.config(SimpleSpark.conf(
      appName,
      master,
      opts
    )).getOrCreate
  }
}

object SimpleGraphX extends BaseEnv{
  val HOME = WORKING_HOME + "/graphx"

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
