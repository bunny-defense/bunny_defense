
package game_mechanics.bunny

import java.io.File
import javax.imageio.ImageIO
import util.Random

import runtime.TowerDefense
import game_mechanics.path._
import game_mechanics.Player
import gui.animations.{GoldAnimation,SmokeAnimation}

/* Spec Op Bunny */

class SpecOpBunny(player_id: Int, bunny_id: Int) extends Bunny
{
    override val id            = bunny_id
    override val player        = player_id
    override val bunny_graphic =
        ImageIO.read(new File(
            getClass().getResource("/mobs/ninja.png").getPath()))
    override val law           = new Random()
    override val price         = 200

	override def update(dt: Double): Unit = {
        if ( this.path.reached ) {
            TowerDefense.gamestate.strategy.updatestrategy.lost_hp(this)
        }
        if ( !this.alive ) {
            TowerDefense.strategy.updatestrategy.on_death(this)
            return
        }
        /* Bunny jump */
       TowerDefense.strategy.updatestrategy.spec_jump(this)
    }
}
