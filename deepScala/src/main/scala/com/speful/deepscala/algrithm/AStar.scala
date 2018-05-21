package com.speful.deepscala.algrithm

import scala.annotation.tailrec
import scala.collection.mutable.{ListBuffer, Map => MMap , Set => MSet }

case class Point( x : Int = -1 , y : Int = -1 , var parent : Option[Point] = None){
  def moveTo( dxy : (Int, Int) ) = Point( dxy._1 + x , dxy._2 + y , parent )

  override def toString: String = s"( $x , $y )"

  override def hashCode(): Int = (x,y).hashCode

  override def equals(obj: Any): Boolean = hashCode == obj.hashCode

}


case class AStar[ T ] (val geo : Array[ Array[T] ] , wall : T , path : T , val step : Int = 10 ) {




  //每个点的G的映射和H的映射
  lazy val GMap,HMap  = MMap[Point , Int]()

  lazy val openList , closeList = MSet[Point]()

  /**
    * 计算当前节点的G值
    * 即父节点G值＋step
    * @param p
    * @return
    */
  def G(p : Point) ={
    val oldG = p.parent map( GMap get _ ) getOrElse None getOrElse 0
    oldG + step
  }


  /**
    * 计算当前节点到目标节点的H值
    * 此处采用曼哈顿距离
    * @param p
    * @param target
    * @return
    */
  def H( p : Point , target : Point ) = ( Math.abs(p.x - target.x) + Math.abs(p.y - target.y) ) * step


  /**
    * 计算当前节点p的F值
    * F = G + H
    * @param p
    * @return
    */
  def F( p : Point ) = GMap.getOrElse (p , 0) + HMap.getOrElse(p , 0)


  def findPathAndShow ( start : Point , target : Point ){

    val len = findPath(start, target).map{ case (x,y) => geo(x)(y) = path }.length

    println(
      s"""
         |from : $start
         |to : $target
         |shortest lenth : ${if(len == 0) "No path" else len}
      """.stripMargin)

    geo.transpose map(_ mkString("  ")) map println

  }
  /**
    * AStar主算法
    * @param start
    * @param target
    * @return
    */
  def findPath( start : Point , target : Point ): List[(Int , Int)]= {

    //初始化openList
    openList += start

    //设置openList终止标记,当openList中存在target时终止
    var stop = false

    while ( openList.nonEmpty && !stop ){
      //F最小的点
      val curp = openList minBy F
      openList -= curp
      closeList += curp

      //找到,没有逃出geo范围的、不是墙的、不是close状态的邻居
      val neighbors = genNeighbors(curp) filter withinGeo filterNot isWall filterNot inClose

      neighbors map{ case p @ Point(x , y , _) =>

        if( openList exists( _ == target ) ){
          val newG = G(p)
          GMap get p filter( _ > newG ) map{ _ =>
            p parent = Some( curp )
            GMap += p -> newG
          }
        }else{
          p parent = Some( curp )
          GMap += p -> G(p)
          HMap += p -> H(p , target)
          openList += p
        }
      }

      stop = openList.exists( _ == target )

    }

    val finalPoint = openList.find( _ == target )

    @tailrec
    def makePaths( point : Option[Point] , res : ListBuffer[(Int,Int)] = ListBuffer.empty ) : List[(Int,Int)] = point match {
      case None => res.result
      case Some( Point( x , y , parent) ) =>
        res += ((x , y))
        makePaths( parent , res )
    }

    makePaths( finalPoint )
  }

  /**
    * 生成四个方向
    * @return
    */
  lazy val fourDir = {
    val li = List( 0, 1, 0, -1, 0 )
    0 :: li zip li tail
  }

  def genNeighbors : Point => List[Point] = fourDir map _.moveTo

  def withinGeo(p : Point) = geo.indices.contains( p x ) && geo.transpose.indices.contains( p y )

  def isWall(p : Point) = geo(p x)(p y) == wall

  def inClose : Point => Boolean = closeList contains _


}

object Demo extends App{

  val raw_geo = List.tabulate[Char](9,9)( ( _ , _ ) => 'O')
  val start = Point( 8 , 6 )
  val target = Point( 5 , 4 )


  val geo1 = raw_geo.toArray.map(_ toArray)
  //设置墙
  Seq((3,3),(4,3),(5,3),(6,3),(7,3),(7,4),(7,5),(7,6),(7,7),(5,6),(6,6)).map{case (x,y) => geo1(x)(y) = 'X'}

  AStar( geo1 , wall = 'X' , path = '-').findPathAndShow(start, target)

  val geo2 = raw_geo.toArray.map(_ toArray)
  Seq((3,3),(4,3),(5,3),(6,3),(7,3),(7,4),(7,5),(7,6),(7,7),(5,6),(6,6),(4,6),(4,5),(4,4)).map{case (x,y) => geo2(x)(y) = 'X'}

  AStar( geo2 , wall = 'X' , path = '-').findPathAndShow(start, target)

}