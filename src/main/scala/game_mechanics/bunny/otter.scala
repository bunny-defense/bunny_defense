
package game_mechanics.bunny

import java.io.File
import javax.imageio.ImageIO

import runtime.GameState
import game_mechanics.JPS
import game_mechanics.Player
import game_mechanics.path._

/* A boss! */
case class Otter(
    _owner: Player,
    bunny_id: Int,
    _path: Progress,
    gamestate: GameState)
extends Bunny(_owner, _path, gamestate)
{
    override val id            = bunny_id
    pos = path.path.head
    override val bunny_graphic =
        ImageIO.read(
            new File(getClass().getResource("/mobs/otter.png").getPath()))
    initial_hp                 = 1000.0
    override val base_shield   = 1.5
    shield                     = 1.5
    base_speed                 = 1.0
    speed                      = 1.0
    override val damage        = 5
    override def reward        = atan_variation(100,15,25)
}
