// What..... is a throw ?
// A bullet... No! A carrot! Ouaahhhhhhh

package game_mechanics

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import runtime.Controller
import game_mechanics.path.Waypoint

object Throw
{
  val carrot_sprite = ImageIO.read(new File(getClass().getResource("/projectiles/carrot.png").getPath()))
}

/* The class of a throw */
class Throw (target:Bunny, origin: Waypoint) {
  import Throw._
  var speed    = 10.0
  var damage   = 5.0
  var AOE      = 0.0
  var pos      = origin

  /* Update of the position of the throw */
  def move(dt : Double): Unit = {
    pos += (target.pos - pos).normalize() * (this.speed * dt)
  }

  /* One step of progress */
  def update(dt: Double): Unit= {
    move(dt)
    if (((pos-target.pos) & pos) <= 0.0)
    {
      target.remove_hp( damage )
      Controller -= this
    }
    if( target.hp <= 0 )
      Controller -= this
  }

  def graphic(): BufferedImage = {
    return carrot_sprite
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
