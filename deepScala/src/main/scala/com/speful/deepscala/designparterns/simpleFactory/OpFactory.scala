package com.speful.deepscala.designparterns.simpleFactory

object OpFactory {
  def <-- : String => Operator = {
    case "+" => new AddOperator
    case "-" => new SubtractOperator
    case "*" => new MultiplyOperator
    case "/" => new DivideOperator
    case ot =>
      println(s"Warning: $ot operator not found,return defalut value 0.")
      new DefaultOperator
  }
}


trait Operator

class AddOperator extends Operator
class SubtractOperator extends Operator
class MultiplyOperator extends Operator
class DivideOperator extends Operator

class DefaultOperator extends Operator