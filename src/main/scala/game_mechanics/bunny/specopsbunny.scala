
package game_mechanics.bunny

import java.io.File
import javax.imageio.ImageIO
import util.Random

import runtime.TowerDefense
import runtime.GameState
import game_mechanics.path._
import game_mechanics.{Player,JPS}
import gui.animations.{GoldAnimation,SmokeAnimation}

/* Spec Op Bunny */

case class SpecOpBunny(
    _owner: Player,
    bunny_id: Int,
    start: CellPos,
    arrival: CellPos,
    gamestate: GameState)
extends Bunny(_owner,gamestate)
{
    override val id            = bunny_id
    override var path = new Progress(
        new JPS(start, arrival, gamestate).run()
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
            gamestate -= this
            gamestate.bunny_reach_goal_strategy(this)
        }
        /* Bunny jump */
        gamestate.spec_ops_jump_strategy(this)
        if( !jumping )
            move(dt)
    }
}
