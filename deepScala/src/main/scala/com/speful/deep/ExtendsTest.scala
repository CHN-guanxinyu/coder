package com.speful.deep

object ExtendsTest extends App{
  c bar //1 2

}


trait Parent{
  def foo =  println(1)


  final def bar= foo
}

object c extends Parent{
  override def foo = {
    super.foo
    println(2)
  }
}