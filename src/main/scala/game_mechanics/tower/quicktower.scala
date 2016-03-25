
package game_mechanics.tower

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object QuickTower extends TowerType
{
    override val tower_graphic  =
        ImageIO.read(
            new File(
                getClass().getResource("/towers/quick_tower.png").getPath()))
    override val range          = 5
    override val spread         = 1.0
    override val throw_cooldown = 0.2
    override val throw_speed    = 20.0
    override val damage         = 1
    override val buy_cost       = 75
    override val sell_cost      = 35
}

