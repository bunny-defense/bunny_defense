
package game_mechanics.tower

import javax.imageio.ImageIO
import java.io.File

import game_mechanics.ProjectileFactory

object SplashTower extends ShooterTower(ProjectileFactory.SPLASH_PROJECTILE)
{
    override val name        = "Turnip catapult"
    override val desc        = "Turnips cause splash damage"
    override val unlock_wave = 10
    override val throw_graphic =
       ImageIO.read(
            new File(
                getClass().getResource("/projectiles/Red-Turnip-icon.png").getPath()))
    override val price       = 2500
    sell_cost     = 2000
}
