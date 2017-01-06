
package game_mechanics.bunny

import runtime.GameState
import game_mechanics.JPS
import game_mechanics.Player
import game_mechanics.path._

case class NormalBunny(
    _owner: Player,
    val bunny_id: Int,
    _path: Progress,
    _health_modifier: Double = 1.0)
extends Bunny(
    _owner,
    _path,
    _health_modifier)
{
    override val id     = bunny_id
    pos = path.path.head
}
