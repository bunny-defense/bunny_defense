package game_mechanics

import game_mechanics._


object Player {
  var hp   = 10
  var gold = 150

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
  }

  def remove_gold(amount : Int) : Boolean = {
    if (this.gold - amount >= 0) {
      this.gold -= amount
      return true
    }
    else {
      return false
    }
  }
}

