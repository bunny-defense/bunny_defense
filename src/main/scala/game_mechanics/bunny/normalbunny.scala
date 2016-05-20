
package game_mechanics.bunny

import runtime.GameState
import game_mechanics.JPS
import game_mechanics.Player
import game_mechanics.path._

case class NormalBunny(
    _owner: Player,
    bunny_id: Int,
    _path: Progress,
    gamestate: GameState)
extends Bunny(_owner, _path, gamestate)
{
    override val id     = bunny_id
    pos = path.path.head
}
