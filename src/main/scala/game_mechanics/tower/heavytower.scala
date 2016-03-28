
package game_mechanics.tower

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object HeavyTower extends TowerType
{
    override val tower_graphic =
        ImageIO.read(
            new File(
                getClass().getResource("/towers/tank.png").getPath()))
    range         = 4
    override val throw_speed   = 15.0
    damage        = 9
    override val buy_cost      = 150
    override val sell_cost     = 75
}
