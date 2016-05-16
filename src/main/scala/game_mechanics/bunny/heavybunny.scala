
package game_mechanics.bunny

import game_mechanics.JPS
import game_mechanics.path._

/* Large and tough but slow bunny */
case class HeavyBunny(player_id: Int, bunny_id: Int, pos: CellPos, arrival: CellPos) extends Bunny
{
    override val id          = bunny_id
    override val player      = player_id
    override val path = new Progress(
        new JPS(pos, arrival).run()
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
