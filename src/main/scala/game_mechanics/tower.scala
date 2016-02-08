
package game_mechanics

import runtime.Controller
import path._ // Really necessary ?
import game_mechanics._
import scala.collection.mutable._
import Math._

/* Abstract tower superclass from which evey tower will be derived */
abstract class Tower(P:Waypoint) {
  val pos = P
  val size = 1
  var damages = 5
  var radius = 100
  var aoe_radius = 0
  var throw_speed = 1
  var throw_cooldown = 10.0
  var cooldown = 0.0
  var sell_cost = 5
  var buy_cost = 10
  var to_sell = false 
  /* The tower keeps a selected target until it goes out of range */
  var current_target = null

  /* Returns whether or not the bunny is in the range of the tower */
  def in_range(B: Bunny): Boolean = {
    return ((B.pos - pos).norm <= radius)
  }

  /* Returns the target of the tower */
  def get_target(): Bunny = {
    if( current_target = null || !in_range(current_target) )
      current_target = Controller.bunnies.filter( in_range ).head
    return current_target
  }

  /* Creates a Throw object, with the characteristics of the tower */
  def attack(B: Bunny): Throw = {
    if( cooldown <= 0 ) {
      var throw_carrot = new Throw(B,this.pos)
      throw_carrot.speed = this.throw_speed
      throw_carrot.damage = this.damages
      throw_carrot.AOE = this.aoe_radius
      return throw_carrot
      cooldown = throw_cooldown /* Resetting the cooldown */
    }
  }

  /* Updates the tower */
  def update(dt: Double): Unit = {
    if( cooldown <= 0 )
      attack( get_target() )
    else
      cooldown -= dt
  }

  def sell_turret(): Unit = {
    this.to_sell = true
  }
}

class QuickTower(P:Waypoint) extends Tower(P) {
  this.radius = 75
  this.throw_speed = 5
  this.damages = 4
  this.buy_cost = 15
  this.sell_cost = 8
}

class HeavyTower(P:Waypoint) extends Tower(P) {
  this.radius = 75
  this.throw_speed = 15
  this.damages = 9
  this.buy_cost = 12
  this.sell_cost = 7
}

/* AOE Tower (spinning scarecrow) */
class ScarecrowTower(P:Waypoint) extends Tower(P) {
  this.radius = 10
  this.damages = 4
  this.buy_cost = 15
  this.sell_cost = 8

  override def try_attack( Bl: ListBuffer[Bunny]): ListBuffer[Throw] = {
    var throws = new ListBuffer[Throw]
    val bunnies : ListBuffer[Bunny] = Bl.filter({
          x => ((x.pos - this.pos).norm <= this.radius)
        })
      if (cooldown == 0 && bunnies.length == 0) {
        cooldown = throw_cooldown
        return (bunnies.map( attack(_)))
      }
      else {
        cooldown -= 1
        return null
      }
    }
  }
