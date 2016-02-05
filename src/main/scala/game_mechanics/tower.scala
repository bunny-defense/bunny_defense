
package game_mechanics

import path._ // Really necessary ?
import game_mechanics._
import scala.collection.mutable._
import Math

/* Abstract tower superclass from which evey tower will be derived */
abstract class Tower {
  val pos : Waypoint
  val size = 1
  val damages = 5
  val radius = 100
  val aoe_radius = 0
  val throw_speed = 1
  val throw_cooldown = 1
  var cooldown = 0
  val sell_cost = 10
  val buy_cost = 10

  /* Creates a Throw object, with the characteristics of the tower */
  def attack(B : Bunny): Unit = {
    Throw_carrot : Throw = new Throw{
                              target = B;
                              speed = this.throw_speed;
                              damage = this.damages;
                              AOE = aoe_radius;
                              pos = this.pos
                            }
  }

  /* Checks if the tower can attack a rabbit. Takes a rabbit list, and
  checks if the cooldown is over */
  def try_attack(Bl = Buffer[Bunny]) : Unit = {
    if (cooldown <= 0) {
      bun = Bl.filter(x => (Math.abs(x.pos - this.pos) < this.radius))
      if not(bun.isEmpty {
          bunny = bun.head
          Attack(bunny)
          cooldown = throw_cooldown
        }
      }
      else {
        cooldown -= 1
      }
    }
