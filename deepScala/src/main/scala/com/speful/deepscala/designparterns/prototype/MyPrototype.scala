package com.speful.deepscala.designparterns.prototype

class MyPrototype(val a : Int ,val b : String ,val p : MyPrototype) extends Cloneable{

  override def clone(): MyPrototype = super.clone().asInstanceOf[MyPrototype]

  override def equals(obj: scala.Any): Boolean = {
    def ii[U] = () => obj.isInstanceOf[U]
    return if( ii[MyPrototype]() ){
      val o = obj.asInstanceOf[MyPrototype]
      o.a == a &&
      o.b == b &&
      o.p == p
    }else false
  }
}
