
package game_mechanics.tower

import game_mechanics.bunny.Bunny
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object SuppBuffTower extends TowerType
{
    override val tower_graphic  =
        ImageIO.read(
            new File(
                getClass().getResource("/towers/radar.gif").getPath()))
    override val throw_graphic  =
        ImageIO.read(
            new File(
                getClass().getResource("/blank.png").getPath()))
    override val base_range     = 5
    range                       = 5
    override val base_damage    = 0
    damage                      = 0
    override val buy_cost       = 50
    override val sell_cost      = 35
    override def allied_effect(tower : Tower) {
        /* The argument is the tower ON WHICH the effect is cast */
        tower.damage += 100
    }
}

