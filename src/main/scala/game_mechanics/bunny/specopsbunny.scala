
package game_mechanics.bunny

import java.io.File
import javax.imageio.ImageIO
import util.Random

import runtime.TowerDefense
import game_mechanics.path._
import game_mechanics.{Player,JPS}
import gui.animations.{GoldAnimation,SmokeAnimation}

/* Spec Op Bunny */

case class SpecOpBunny(
    gamestate: GameState,
    player_id: Int, bunny_id: Int, pos: CellPos, arrival: CellPos)
extends Bunny
{
    override val id            = bunny_id
    override val player        = player_id
    override val path = new Progress(
        new JPS(pos, arrival).run()
                    match {
                        case None    => throw new Exception()
                        case Some(p) => p
                    }
                    )
    override val bunny_graphic =
        ImageIO.read(new File(
            getClass().getResource("/mobs/ninja.png").getPath()))
    override val law           = new Random()
    override val price         = 200
    var jumping                = false

	override def update(dt: Double): Unit = {
        if ( this.path.reached ) {
            TowerDefense.gamestate.strategy.updatestrategy.lost_hp(this)
        }
        /* Bunny jump */
        TowerDefense.gamestate.strategy.updatestrategy.spec_jump(this,dt)
        if( !jumping )
            move(dt)
    }
}
