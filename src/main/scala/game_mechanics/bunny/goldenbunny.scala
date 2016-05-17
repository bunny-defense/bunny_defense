
package game_mechanics.bunny

import runtime.GameState
import java.io.File
import javax.imageio.ImageIO
import game_mechanics.path._
import game_mechanics.Player
import game_mechanics.JPS

/* Rare golden bunny worth a lot of money */
case class GoldenBunny(
    _owner: Player,
    bunny_id : Int,
    start: CellPos,
    arrival: CellPos,
    gamestate : GameState)
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
            getClass().getResource("/mobs/goldenbunny_alt1.png").getPath()))
    initial_hp                 = 20.0
    base_speed                 = 8.0
    speed                      = 8.0
    override val price         = 10000000
    override def reward        = atan_variation(500,500,1) /* Constant at 500 */
    override val damage        = 0
}
