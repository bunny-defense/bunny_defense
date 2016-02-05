
package game_mechanics

import path._ // Really necessary ?
import game_mechanics._
import scala.collection.mutable._
import Math._

/* Abstract tower superclass from which evey tower will be derived */
abstract class Tower {
  val pos : Waypoint
  val size = 1
  var damages = 5
  var radius = 100
  var aoe_radius = 0
  var throw_speed = 1
  var throw_cooldown = 1
  var cooldown = 0
  var sell_cost = 5
  var buy_cost = 10
  var to_sell = false 

  /* Creates a Throw object, with the characteristics of the tower */
  def attack(B : Bunny): Throw = {
    var throw_carrot = new Throw(B,this.pos)
    throw_carrot.speed = this.throw_speed
    throw_carrot.damage = this.damages
    throw_carrot.AOE = this.aoe_radius
    return throw_carrot
  }

  /* Checks if the tower can attack a rabbit. Takes a rabbit list, and
  checks if the cooldown is over */
  def try_attack(Bl : Buffer[Bunny]) : Throw = {
      val bunnies : Buffer[Bunny] = Bl.filter({
            x => ((x.pos - this.pos).norm <= this.radius)
          })
      if ( cooldown == 0 && bunnies.length == 0) {
          cooldown = throw_cooldown
          return (attack(bunnies.head))
        }
      else {
        cooldown -= 1
        return null
      }
    }
  }
