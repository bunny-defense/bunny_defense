
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
    val bunny_id: Int,
    _path: Progress,
    _gamestate: GameState,
    _health_modifier: Double = 1.0)
extends Bunny(_owner, _path, _gamestate, _health_modifier)
{
    override val id          = bunny_id
    pos = path.path.head
    override val bunny_graphic =
        ImageIO.read(new File(
            getClass().getResource("/mobs/hare_alt1.png").getPath()))
    override val base_hp  = 5.0
    override val base_shield = 0.0
    shield                   = 0.0
    base_speed               = 4.0
    speed                    = 4.0
}
