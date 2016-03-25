
package game_mechanics.tower

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/* AOE Tower (spinning scarecrow) */
object ScarecrowTower extends TowerType
{
    override val tower_graphic =
        ImageIO.read(
            new File(
                getClass().getResource("/towers/heavy_tower.png").getPath()))
    override val range     = 4
    override val damage    = 6
    override val buy_cost  = 1500
    override val sell_cost = 80
    override val aoe_radius = 10
}
