
package game_mechanics.bunny

import java.io.File
import javax.imageio.ImageIO
import util.Random

import runtime.Controller
import game_mechanics.path._
import game_mechanics.Player
import gui.animations.{GoldAnimation,SmokeAnimation}

/* Spec Op Bunny */

class SpecOpBunny(player_id: Int) extends Bunny
{
    override val player        = player_id
    override val bunny_graphic =
        ImageIO.read(new File(
            getClass().getResource("/mobs/ninja.png").getPath()))
    override val price         = 200

    override def update(dt: Double): Unit = {
        if ( !this.alive ) {
            Controller += new GoldAnimation(
                this.reward(Controller.wave_counter),
                this.pos.clone()
            )
            Player.add_gold( this.reward( Controller.wave_counter ))
            Controller -= this
            Player.killcount += 1
            return
        }
        /* Bunny jump */
        if (law.nextDouble < 1.0/180.0 ) {
            Controller -= this
            val anim = new SmokeAnimation(this.pos)
            anim and_then { () =>
                this.path.random_choice
                this.pos = this.path.get_position()
                println(this.path.toString)
                Controller += this
                Controller += new SmokeAnimation(this.pos)
            }
            Controller += anim
        }
        else {
            this.move(dt)
            if ( this.path.reached ) {
                Player.remove_hp( this.damage )
                Controller -= this
            }
        }
    }
}
