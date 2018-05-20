package com.speful.deepscala.designparterns.decorator

abstract class GodDecorator(baby : BabyComponent) extends BabyComponent {
  override def comeOn: Unit = baby comeOn
}

object AddSomeCute{
  def apply(baby : BabyComponent) = new AddSomeCute( baby )
}
private[designParterns] class AddSomeCute(baby : BabyComponent) extends GodDecorator(baby) {
  override def comeOn: Unit = {
    println("add some cute...")
    super.comeOn
  }
}
object AddLotsOfGoodVoice{
  def apply(baby: BabyComponent): AddLotsOfGoodVoice = new AddLotsOfGoodVoice(baby)
}
private[designParterns] class AddLotsOfGoodVoice(baby: BabyComponent) extends GodDecorator(baby){
  override def comeOn: Unit = {
    println("add lots of good voice...")
    super.comeOn
  }
}

trait BabyComponent {
  def comeOn
}


