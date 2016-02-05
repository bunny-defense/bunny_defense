package game_mechanics

import game_mechanics._


class Player {
  var hp = 10
  var gold = 150

  def remove_hp(): Unit = {
    this.hp -= 1
  }

  def add_hp(amount : Int): Unit = {
    this.hp += 1
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

