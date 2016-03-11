package game_mechanics

import swing._
import swing.event._

import game_mechanics._

case object MoneyChanged extends Event

object Player extends Publisher {
  var hp   = 10
  var gold = 150
  var killcount = 0

  def reset(): Unit = {
    hp   = 10
    gold = 150
  }

  def remove_hp(amount: Int): Unit = {
    this.hp -= amount
  }

  def add_hp(amount : Int): Unit = {
    this.hp += amount
  }

  def add_gold(amount : Int) : Unit = {
    this.gold += amount
    publish( MoneyChanged )
  }

  def remove_gold(amount : Int) : Boolean = {
    if (this.gold - amount >= 0)
    {
      this.gold -= amount
      publish( MoneyChanged )
      return true
    }
    else
      return false
  }
}

