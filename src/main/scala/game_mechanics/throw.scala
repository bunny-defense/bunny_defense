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
  var speed    = 1.0
  var damage   = 5.0
  var AOE      = 0.0
  var pos      = origin
  var hit      = false

  /* Update of the position of the throw */
  def move(dt : Double): Unit = {
    val next_pos = pos + (target.pos - pos).normalize() * (speed * dt)
    hit = ((target.pos - pos) & (target.pos - next_pos)) < 0.0
    pos = next_pos
  }

  /* One step of progress */
  def update(dt: Double): Unit= {
    move(dt)
    if (hit)
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
}
