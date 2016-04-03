
package game_mechanics.tower

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import game_mechanics.ProjectileFactory

object HeavyTower extends ShooterTower(ProjectileFactory.BASE_PROJECTILE)
{
    override val name = "Heavy tower"
    override val desc = "Shoots heavy carrots at a slow rate"
    override val tower_graphic =
        ImageIO.read(
            new File(
                getClass().getResource("/towers/tank.png").getPath()))
    base_range                 = 4
    range                      = 4
    override val throw_speed   = 15.0
    base_damage                = 9
    damage                     = 9
    override val buy_cost      = 150
    override val sell_cost     = 75
    override val unlock_wave   = 3
}

