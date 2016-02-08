// What..... is a throw ?
// A bullet... No! A carrot! Ouaahhhhhhh

package game_mechanics

import game_mechanics.path.Waypoint

/* The abstract class of a throw */

class Throw (B:Bunny, T: Waypoint) {
    var target   = B
    var speed    = 1.0
    var damage   = 5.0
    var AOE      = 0.0
    var pos      = T

/* Update of the position of the throw */
    def update_pos(dt : Double): Unit = {
      val a = this.pos
      this.pos += (this.target.pos - this.pos) * dt * this.speed
      if (((this.pos-this.target.pos) & a) <= 0.0) {
          this.pos = this.target.pos
        }
    }

/* One step of progress */
    def progress(dt: Double): Unit= {
      update_pos(dt,dir)
      if (this.target.pos == this.pos) {
        target.takedamage(this.damage)
      }
    }

    var time_to_touch = 1

    def auto_touch(dt : Double): Unit = {
      if (time_to_touch == 0) {
      this.pos = this.target.pos
    }
    else {
      time_to_touch -= dt
    }
  }
}
