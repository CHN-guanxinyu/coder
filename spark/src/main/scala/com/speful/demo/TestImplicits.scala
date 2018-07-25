package com.speful.demo

import com.speful.implicits._
trait Foo{
  def bar
}
class FooImpl extends Foo{
  def bar = println("bar")
}
object TestImplicits extends App {
  "com.speful.demo.FooImpl".as[Foo].bar
}
