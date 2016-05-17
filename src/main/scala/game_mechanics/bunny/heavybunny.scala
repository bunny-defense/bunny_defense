
package game_mechanics.bunny

import runtime.GameState
import game_mechanics.JPS
import game_mechanics.Player
import game_mechanics.path._

/* Large and tough but slow bunny */
case class HeavyBunny(
    _owner: Player,
    bunny_id: Int,
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
    override val initial_hp  = 20.0
    override val base_shield = 1.5
    shield                   = 1.5
    override val base_speed  = 1.0
    speed                    = 1.0
    override val price       = 15
    override def reward      = atan_variation(10,2,10)
}
