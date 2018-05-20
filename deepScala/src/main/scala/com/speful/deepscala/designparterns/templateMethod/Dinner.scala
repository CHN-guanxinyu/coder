package com.speful.deepscala.designparterns.templateMethod

abstract class Dinner {
  final def eat(){
    cooking
    Thread sleep 1000
    eating
    Thread sleep 1000
    brushing
    Thread sleep 1000
  }
  protected def cooking
  protected def eating
  protected def brushing

}

class ChineseDinner extends Dinner {
  override def cooking: Unit = println("中国美食")

  override def eating: Unit = println("中国式吃饭")

  override def brushing: Unit = println("中国式刷碗")
}
class KoreanDinner extends Dinner {
  override def cooking: Unit = println("准备狗粮")

  override def eating: Unit = println("趴在马桶里吃")

  override def brushing: Unit = println("按下冲水按钮")
}