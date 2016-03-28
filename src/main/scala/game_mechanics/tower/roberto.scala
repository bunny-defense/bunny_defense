
package game_mechanics.tower

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/* Human commando in a fishtank who uses phychic powers to continuouly hurt nearby rabbits (metal as hell) */
object Roberto extends TowerType
{
    override val tower_graphic =
        ImageIO.read(
            new File(
                getClass().getResource("/towers/roberto.png").getPath()))
    override val throw_graphic =
        ImageIO.read(
            new File(
                getClass().getResource("/blank.png").getPath()))
    range     = 6
    damage    = 1
    override val buy_cost  = 3000
    override val sell_cost = 150
    override val throw_speed = 100.0
    override val throw_cooldown = 0.05
}