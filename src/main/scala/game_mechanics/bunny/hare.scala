
package game_mechanics.bunny

import java.io.File
import javax.imageio.ImageIO

import game_mechanics.JPS
import game_mechanics.path._
import runtime.GameState._

/* Fast "Bunny" */
class Hare(
    player_id: Int,
    bunny_id : Int,
    pos: CellPos,
    arrival: CellPos,
    gamestate: GameState) extends Bunny
{
    override val id          = bunny_id
    override val player      = player_id
    override val path = new Progress(
        new JPS(pos, arrival).run(gamestate)
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
