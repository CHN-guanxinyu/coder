package com.speful.deepscala.designparterns.strategy

class AnimalContext(var _animal : Animal ) {
  def animal = _animal
  def animal( a : Animal ) = _animal = a
  def eatThenSpeak = {
    eat
    speak
  }
  def speakThenEat = {
    speak
    eat
  }
  def speak = animal speak
  def eat = animal eat
}

class AnimalContextWithSimpleFactory( var _animal : String ){
  private def animal : Animal = _animal match {
    case "Cat" => new Cat
    case "Dog" => new Dog
    case a => throw new IllegalArgumentException(s"animal '$a' not found")
  }
  def speak = animal speak
  def eat = animal eat
}

trait Animal{
  def speak
  def eat
}

class Cat extends Animal{
  override def speak: Unit = println( "喵" )

  override def eat: Unit = println("吃了口猫粮")
}
class Dog extends Animal {
  override def speak: Unit = println( "汪" )

  override def eat: Unit = println("吃了口狗粮")
}