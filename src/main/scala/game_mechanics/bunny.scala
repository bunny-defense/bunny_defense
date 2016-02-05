
package game_mechanics

import game_mechanics.path._

/* Abstract bunny superclass from which every ennemy is derived. */
abstract class Bunny {

  var hp              = 10.0
  var pos : Waypoint  = new Waypoint(2.0,2.0)
  var shield          = 1.0
  var speed           = 1.0
  var path            = new Path()
  var reward          = 10

  def takedamage(dmg: Double): Unit = {
    this.hp -= dmg
  }
}

/* Large and tough but slow bunny */
class Heavy_Bunny extends Bunny {
  this.hp     = 20
  this.shield = 1.5
  this.speed  = 0.5
  this.reward = 15
}

class Otter extends Bunny {
  this.hp     = 100
  this.shield = 1.5
  this.speed  = 0.5
  this.reward = 100
}
