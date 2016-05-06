package game_mechanics

import swing._
import swing.event._

import game_mechanics._
import utils.Parameters

case object MoneyChanged extends Event

object Player extends Publisher {
  var id   = 0
  var hp   = Parameters.player_initial_life
  var gold = Parameters.player_initial_gold
  var killcount = 0

  def reset(): Unit = {
    hp   = Parameters.player_initial_life
    gold = Parameters.player_initial_gold
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
