
package game_mechanics.tower

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import game_mechanics.ProjectileFactory

object QuickTower extends ShooterTower(ProjectileFactory.BASE_PROJECTILE)
{
    override val name = "Quick tower"
    override val desc = "Shoots light carrots at a very fast rate"
    override val tower_graphic  =
        ImageIO.read(
            new File(
                getClass().getResource("/towers/quick_tower.png").getPath()))
    override val throw_graphic  =
        ImageIO.read(
            new File(
                getClass().getResource("/projectiles/leaf.png").getPath()))
    base_range                  = 5
    range                       = 5
    override val spread         = 1.0
    override val throw_cooldown = 0.2
    override val throw_speed    = 20.0
    base_damage                 = 1
    damage                      = 1
    override val buy_cost       = 75
    sell_cost                   = 60
    override val unlock_wave    = 1
}

