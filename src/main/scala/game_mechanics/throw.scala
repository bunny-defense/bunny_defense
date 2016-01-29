// What..... is a throw ?
// A bullet... No! A carrot! Ouaahhhhhhh

package game_mechanics

import game_mechanics.path.Waypoint

/* The abstract class of a throw */

abstract class Throw {
    var target   : Waypoint
    val speed    = 1.0
    val damage   = 5.0
    val AOE      = 0.0
    var pos      : Waypoint
    var hit      = false

/* Update of the position of the throw */
    def update_pos(dt : Int, dir: Waypoint): Unit = {
      this.pos += this.speed * (this.target - this.pos) * dir
    }

/* One step of progress */
    def progress(dt: Int): Unit= {
      update_pos(dt)
      this.hit = (target.x == pos.x && target.y == pos.y)
    }
  }
