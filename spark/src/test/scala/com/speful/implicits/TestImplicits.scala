package com.speful.implicits
import org.scalatest.FlatSpec
import com.speful.implicits._
trait Foo {
  def bar : String
}
class FooImpl extends Foo{
  override def bar = "bar"
}
class TestImplicits extends FlatSpec{


  behavior of "An empty Set"

  it should "bar" in {
    assert {
      "bar" == "com.speful.implicits.FooImpl".as[Foo].bar
    }
  }
}
