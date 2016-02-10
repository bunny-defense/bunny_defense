// What..... is a throw ?
// A bullet... No! A carrot! Ouaahhhhhhh

package game_mechanics

import runtime.Controller
import game_mechanics.path.Waypoint

/* The abstract class of a throw */

class Throw (target:Bunny, origin: Waypoint) {
  var speed    = 1.0
  var damage   = 5.0
  var AOE      = 0.0
  var pos      = origin

  /* Update of the position of the throw */
  def move(dt : Double): Unit = {
    pos += (target.pos - pos).normalize() * speed * dt
  }

  /* One step of progress */
  def update(dt: Double): Unit= {
    move(dt)
    if (((pos-target.pos) & pos) <= 0.0)
    {
      target.remove_hp( damage )
      Controller -= this
    }
  }

  /*

  var time_to_touch = 1

  def auto_touch(dt : Double): Unit = {
    if (time_to_touch == 0) {
      this.pos = this.target.pos
    }
    else {
      time_to_touch -= dt
    }
  }

   */
}
