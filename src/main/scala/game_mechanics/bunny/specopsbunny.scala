
package game_mechanics.bunny

import java.io.File
import javax.imageio.ImageIO
import util.Random

import runtime.Controller
import game_mechanics.path._
import game_mechanics.Player
import gui.{GoldAnimation,SmokeAnimation}

/* Spec Op Bunny */

object SpecOpBunny extends BunnyType
{
    override val bunny_graphic =
        ImageIO.read(new File(
            getClass().getResource("/mobs/ninja.png").getPath()))
    val law = new Random()

	override def update(bunny: Bunny, dt: Double): Unit = {
        if ( !bunny.alive ) {
            Controller += new GoldAnimation(
                bunny.bunny_type.reward(Controller.wave_counter),
                bunny.pos.clone()
            )
            Player.add_gold( bunny.bunny_type.reward( Controller.wave_counter ))
            Controller -= bunny
            Player.killcount += 1
            return
        }
        /* Bunny jump */
        if (law.nextDouble < 1.0/180.0 ) {
            Controller -= bunny
            val anim = new SmokeAnimation(bunny.pos)
            anim and_then { () =>
                bunny.path.random_choice
                bunny.pos = bunny.path.get_position()
                Controller += bunny
                Controller += new SmokeAnimation(bunny.pos)
            }
            Controller += anim
        }
        else {
            bunny.move(dt)
        }
        if ( bunny.path.reached ) {
            Player.remove_hp( bunny.bunny_type.damage )
            Controller -= bunny
        }
    }
}
