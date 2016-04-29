
package game_mechanics.bunny

import java.io.File
import javax.imageio.ImageIO
import util.Random

import runtime.TowerDefense
import game_mechanics.path._
import game_mechanics.Player
import gui.animations.{GoldAnimation,SmokeAnimation}

/* Spec Op Bunny */

class SpecOpBunny extends Bunny
{
    override val bunny_graphic =
        ImageIO.read(new File(
            getClass().getResource("/mobs/ninja.png").getPath()))
    override val law = new Random()

	override def update(dt: Double): Unit = {
        if ( this.path.reached ) {
            Player.remove_hp( this.damage )
            TowerDefense.gamestate -= this
        }
        if ( !this.alive ) {
            TowerDefense.gamestate += new GoldAnimation(
                this.reward(TowerDefense.gamestate.wave_counter),
                this.pos.clone()
            )
            Player.add_gold( this.reward( TowerDefense.gamestate.wave_counter ))
            TowerDefense.gamestate -= this
            Player.killcount += 1
            return
        }
        /* Bunny jump */
        if (law.nextDouble < 1.0/180.0 ) {
            TowerDefense.gamestate -= this
            val anim = new SmokeAnimation(this.pos)
            anim and_then { () =>
                this.path.random_choice
                this.pos = this.path.get_position()
                println(this.path.toString)
                TowerDefense.gamestate += this
                TowerDefense.gamestate += new SmokeAnimation(this.pos)
            }
            TowerDefense.gamestate += anim
        }
        else {
            this.move(dt)
        }
    }
}
