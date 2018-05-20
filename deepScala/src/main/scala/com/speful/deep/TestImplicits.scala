package com.speful.deep

import com.speful.deep.implicits._

/**
  * 测试隐式转换的一种方式
  *
  */
object TestImplicits extends App {

  //wbe中只有一个myFun方法
  val wbe = new WannaBeExtends( 2 )

  wbe.myFun // 2

  println( wbe.getClass.getSimpleName ) //WannaBeExtends

  //转换
  val res = wbe.extendsFun //extends fun | 3

  println( res.getClass.getSimpleName ) //ResultOfExtends


}