package com.speful.deepscala.tests

/**
  * 测试将List(a,b,c,d,e)变为
  * List( (a,b) , (b,c) , (c,d) , (d,e) )的几种方式
  */

object TestPair extends App{
  val dataSize = 1000000
  val maxIterations = 10

  val data = 1 to dataSize toList

  Timer init

  warmUp(dataSize)

  Timer howLong format("warmUp")

  val t = test(data)

  println(
    t.scanFoo.take(5) + "\n",
    t.slidingFoo.take(5) + "\n" ,
    t.listApiFoo.take(5) + "\n",
    t.foldFoo.take(5)
  )

  (1 to maxIterations).foreach{ i =>
    t.scanFoo
    Timer howLong format(s"scanFoo_$i")
  }


  (1 to maxIterations).foreach{ i =>
    t.slidingFoo
    Timer howLong format(s"slidingFoo_$i")
  }






  (1 to maxIterations).foreach{ i =>
    t.listApiFoo
    Timer howLong format(s"listApiFoo_$i")
  }

  (1 to maxIterations).foreach{ i =>
    t.foldFoo
    Timer howLong format(s"foldFoo_$i")
  }




  def format(name : String) = (time : Long) => s"${name} : ${time} ms"

  def warmUp(i: Int){
    test(1 to i toList) scanFoo
  }
}



case class test[T](data : List[T]){
  def scanFoo ={
    implicit class Slider[T]( li : List[ T ] ) {
      def |\| =
        li.drop(2).scan( li head , li.tail head ){
          case ( ( _ , preSecond ) , nextElem ) => (preSecond , nextElem)
        } map(_.asInstanceOf[(T,T)])
    }
    data |\|
  }


  def slidingFoo ={
    implicit class Slider2[T]( li : List[ T ] ){
      def |\| = li sliding 2 map{case f :: s :: tails => (f , s) } toList
    }

    data |\|
  }

  /**
    * best way
    * @return
    */
  def listApiFoo ={
    implicit class Slider3[T]( li : List[ T ] ){
      def |\| = null.asInstanceOf[T] :: li zip li tail
    }

    data |\|
  }

  def foldFoo ={
    implicit class Slider4[T]( li : List[ T ] ){

      def |\| = (((li head , li.tail head) :: Nil) /: li.drop(2)){
        case ( list @ ( _ , second ) :: _ , elem ) => ( second , elem ) :: list
      } reverse

    }

    data |\|
  }
}



object Timer{
  private var lastTime : Long = _

  private def now = System currentTimeMillis

  def init = lastTime = now

  def howLong : Unit = howLong(time => s"${time}ms")

  def howLong( formatFun : Long  => String )= {
    val t = now
    println(formatFun(t - lastTime))
    lastTime = now
  }
}