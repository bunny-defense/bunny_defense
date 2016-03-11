// What..... is a throw ?
// A bullet... No! A carrot! Ouaahhhhhhh

package game_mechanics

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import runtime.Controller
import game_mechanics.path.Waypoint
import gui.DamageAnimation

object Throw
{
    val carrot_sprite = ImageIO.read(
        new File(
            getClass().getResource("/projectiles/carrot.png").getPath()))
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
            Controller += new DamageAnimation( damage, pos.clone() )
            Controller -= this
        }
      if( target.hp <= 0 )
      {
        Controller -= this
        if (hit)
          /* The bunny is killed by this throw only if it hit ! */
        {
          Player.killcount += 1
        }
      }
    }

    def graphic(): BufferedImage = {
        return carrot_sprite
    }
}
