
package game_mechanics.bunny

import runtime.GameState
import game_mechanics.JPS
import game_mechanics.Player
import game_mechanics.path._

case class NormalBunny(
    _owner: Player,
    bunny_id: Int,
    start: CellPos,
    arrival: CellPos,
    gamestate: GameState)
extends Bunny(_owner,gamestate)
{
    override val id     = bunny_id
    override var path = new Progress(
        new JPS(start, arrival, gamestate).run()
                    match {
                        case None    => throw new Exception()
                        case Some(p) => p
                    }
                    )
}
