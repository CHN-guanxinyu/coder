package com.speful.deepscala.designparterns.factoryMethod

import com.speful.deepscala.designparterns.simpleFactory._

trait OpFactory {
  def get : Operator
}

object AddFactory extends OpFactory {
  override def get: Operator = new AddOperator
}

object SubtractFactory extends OpFactory {
  override def get: Operator = new SubtractOperator
}

object MultiplyFactory extends OpFactory {
  override def get: Operator = new MultiplyOperator
}

object DivideFactory extends OpFactory {
  override def get: Operator = new DivideOperator
}

object DefaultFactory extends OpFactory {
  override def get: Operator = new DefaultOperator
}