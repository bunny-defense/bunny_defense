
package game_mechanics.bunny

import java.io.File
import javax.imageio.ImageIO

import game_mechanics.JPS
import game_mechanics.path._

/* A boss! */
case class Otter(player_id: Int, bunny_id: Int, pos: CellPos, arrival: CellPos) extends Bunny
{
    override val id            = bunny_id
    override val player        = player_id
    override val path = new Progress(
        new JPS(pos, arrival).run()
                    match {
                        case None    => throw new Exception()
                        case Some(p) => p
                    }
                    )
    override val bunny_graphic =
        ImageIO.read(
            new File(getClass().getResource("/mobs/otter.png").getPath()))
    override val initial_hp    = 1000.0
    override val base_shield   = 1.5
    shield                     = 1.5
    override val base_speed    = 1.0
    speed                      = 1.0
    override val damage        = 5
    override val price         = 500
    override def reward        = atan_variation(100,15,25)
}
