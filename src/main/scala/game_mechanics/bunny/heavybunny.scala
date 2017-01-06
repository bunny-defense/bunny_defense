
package game_mechanics.bunny

import runtime.GameState
import game_mechanics.JPS
import game_mechanics.Player
import game_mechanics.path._

/* Large and tough but slow bunny */
case class HeavyBunny(
    _owner: Player,
    val bunny_id: Int,
    _path: Progress,
    _health_modifier: Double = 1.0)
extends Bunny(
    _owner,
    _path,
    20.0,
    _health_modifier)
{
    override val id          = bunny_id
    pos = path.path.head
    override val base_shield = 1.5
    shield                   = 1.5
    base_speed               = 1.0
    speed                    = 1.0
    override def reward      = atan_variation(10,2,10)
}
