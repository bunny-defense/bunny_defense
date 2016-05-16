
package game_mechanics.bunny

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import game_mechanics.JPS
import game_mechanics.path._

case class ShieldBunny(player_id: Int, bunny_id: Int, pos: CellPos, arrival: CellPos) extends Bunny
{
    override val id           = bunny_id
    override val player       = player_id
    override val path = new Progress(
        new JPS(pos, arrival).run()
                    match {
                        case None    => throw new Exception()
                        case Some(p) => p
                    }
                    )
    override val bunny_graphic =
        ImageIO.read(
            new File(getClass().getResource("/mobs/shield_bunny.png").getPath()))
    override val effect_range = 2
    val shield_increase       = 1.5
    override val  price       = 50
    override def allied_effect(bunny: Bunny): Unit = {
        bunny.shield *= 1.5
    }
}
