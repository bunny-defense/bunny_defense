
package game_mechanics.bunny

import java.io.File
import javax.imageio.ImageIO

import runtime.GameState
import game_mechanics.JPS
import game_mechanics.Player
import game_mechanics.path._

/* Fast "Bunny" */
class Hare(
    _owner: Player,
    bunny_id : Int,
    start: CellPos,
    arrival: CellPos,
    gamestate: GameState)
extends Bunny(_owner,gamestate)
{
    override val id          = bunny_id
    override var path = new Progress(
        new JPS(start, arrival, gamestate).run()
                    match {
                        case None    => throw new Exception()
                        case Some(p) => p
                    }
                    )
    override val bunny_graphic =
        ImageIO.read(new File(
            getClass().getResource("/mobs/hare_alt1.png").getPath()))
    override val initial_hp  = 5.0
    override val base_shield = 0.0
    shield                   = 0.0
    override val base_speed  = 4.0
    speed                    = 4.0
    override val price       = 100
}
