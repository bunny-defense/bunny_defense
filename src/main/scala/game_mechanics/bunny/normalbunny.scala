
package game_mechanics.bunny

import game_mechanics.path._
import game_mechanics.JPS


case class NormalBunny(player_id: Int, bunny_id: Int, pos: CellPos, arrival: CellPos) extends Bunny
{
    override val id     = bunny_id
    override val player = player_id
    override val path = new Progress(
        new JPS(pos, arrival).run()
                    match {
                        case None    => throw new Exception()
                        case Some(p) => p
                    }
                    )
}
