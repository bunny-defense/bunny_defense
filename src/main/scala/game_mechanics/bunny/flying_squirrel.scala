
package game_mechanics.bunny

import java.io.File
import javax.imageio.ImageIO

import runtime.GameState
import game_mechanics.JPS
import game_mechanics.Player
import game_mechanics.path._

/* Fast "Bunny" */
case class FlyingSquirrel(
    _owner: Player,
    bunny_id: Int,
    start: CellPos,
    _target: Player,
    gamestate: GameState)
extends Bunny(_owner,gamestate)
{
    override val id          = bunny_id
    override val target      = _target
    override var path = new Progress(
        new JPS(start, target.base, gamestate).run()
                    match {
                        case None    => throw new Exception()
                        case Some(p) => p
                    }
                    )
    override val bunny_graphic =
        ImageIO.read(new File(
            getClass().getResource("/mobs/Flying_Squirrel.png").getPath()))
    initial_hp               = 5.0
    override val base_shield = 0.0
    shield                   = 0.0
    base_speed               = 5.0
    speed                    = 5.0
}
