
package game_mechanics.tower

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object BaseSpawnerTower extends SpawnerTower()
{
    override val name = "Base spawner"
    override val desc = "Expected : 2 normal bunnies, 1 ninja bunny"
    override val tower_graphic =
        ImageIO.read(
            new File(
                getClass().getResource("/towers/tank.png").getPath()))
    base_range                 = 4
    range                      = 4
    override val throw_speed   = 15.0
    base_damage                = 9
    damage                     = 9
    override val price         = 1
    sell_cost                  = 75
    override val unlock_wave   = 1
}
