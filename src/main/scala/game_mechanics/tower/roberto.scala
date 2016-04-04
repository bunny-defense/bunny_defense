
package game_mechanics.tower

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import game_mechanics.ProjectileFactory

/* Human commando in a fishtank who uses phychic powers to continuouly hurt
nearby rabbits (metal as hell) */

object Roberto extends ShooterTower(ProjectileFactory.BASE_PROJECTILE)
{
    override val name = "Roberto"
    override val desc = "A psy soldier in a glass container"
    override val tower_graphic =
        ImageIO.read(
            new File(
                getClass().getResource("/towers/roberto.png").getPath()))
    override val throw_graphic =
        ImageIO.read(
            new File(
                getClass().getResource("/blank.png").getPath()))
    base_range                  = 6
    range                       = 6
    base_damage                 = 1
    damage                      = 1
    override val buy_cost       = 3000
    sell_cost                   = 2400
    override val throw_speed    = 100.0
    override val throw_cooldown = 0.05
    override val unlock_wave    = 20
}
