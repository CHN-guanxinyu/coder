package com.speful.sparkall.example

import java.awt.image.{BufferedImage, DataBufferInt}
import java.awt.{Color, Image}
import java.io.File

import com.speful.sql.utils.SimpleSpark
import javax.imageio.ImageIO
import org.apache.spark.graphx.{Edge, Graph}
import org.apache.spark.mllib.clustering.PowerIterationClustering
import org.apache.spark.mllib.clustering.PowerIterationClustering.Assignment

/**
  * 无法单机运行
  * TODO : 原图片1024*683，笛卡尔积生成庞大数量的Edge对象，导致内存剧增，需解决
  */
object TestCV extends App  with SimpleSpark{



  val path = args(0)

  val file = new File(s"$path/rabbit.jpg")

  println(file.exists())

  val imgFile = ImageIO read file


  val img = imgFile.getScaledInstance(
    imgFile.getWidth / 8, imgFile.getHeight / 8,
    Image SCALE_AREA_AVERAGING
  )

  val (w, h) = (img getWidth null, img getHeight null)

  val bi = new BufferedImage(w, h, BufferedImage TYPE_INT_RGB)

  bi.getGraphics.drawImage(img, 0, 0, null)

  val data = bi.getData.getDataBuffer.asInstanceOf[DataBufferInt].getData


  sc.setCheckpointDir( args(1) )
  val r = sc.makeRDD(data, 14).zipWithIndex.cache

  val edgesRdd = r.cartesian(r).mapPartitions { it =>
    def toVec(a: Tuple2[Int, Long]): Array[Double] = {
      val c = new Color(a._1)
      Array(c.getRed, c.getGreen, c.getBlue)
    }

    def cosineSimilarity(u: Array[Double], v: Array[Double]): Double = {
      val d = Math.sqrt(u.map(e => e * e).sum * v.map(e => e * e).sum)
      if (d == 0.0) 0.0 else
        u.zip(v).map(e => e._1 * e._2).sum / d
    }

    it.map { x =>
      Edge(x._1._2, x._2._2, cosineSimilarity(toVec(x._1), toVec(x._2)))
    } filter (e => e.attr > 0.5)
  }.cache

  val g = Graph.fromEdges(
    edgesRdd, 0.0
  )

  g.checkpoint
  val m = new PowerIterationClustering() run g


  val white$black = Array(Color.white getRGB, Color.black getRGB)

  val newBi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)

  m.assignments.map { case Assignment(id, cluster) =>
    (id / w, (id % w, white$black(cluster)))
  }.groupByKey.map { a =>
    (a._1, a._2.toArray.sortBy(_ _1).map(_ _2))
  }.collect.foreach { x =>
    newBi.setRGB(0, x._1 toInt, w, 1, x._2, 0, w)
  }

  ImageIO.write(newBi, "PNG", new File(s"$path/CVres.png"))


}
