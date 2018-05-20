package com.speful.deep.implicits

case class ResultOfExtends( @transient wbe: WannaBeExtends ){
  def extendsFun = {
    println("extends fun")
    println(wbe.a + 1)
    this
  }
}

trait Conversions{
  implicit def convert( wbe : WannaBeExtends ) : ResultOfExtends = ResultOfExtends(wbe)
}
