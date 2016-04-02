
package game_mechanics.bunny

import java.io.File
import javax.imageio.ImageIO
import util.Random

import runtime.Controller
import game_mechanics.path._
import game_mechanics.Player
import gui.GoldAnimation

/* Spec Op Bunny */

object SpecOpBunny extends BunnyType
{
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
        }
        if (law.nextDouble < 1.0/180.0 ) {
            bunny.path.random_choice
            bunny.pos = bunny.path.get_position()
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
