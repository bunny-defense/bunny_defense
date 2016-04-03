
package game_mechanics.tower

import game_mechanics.ProjectileFactory

object SplashTower extends ShooterTower(ProjectileFactory.SPLASH_PROJECTILE)
{
    override val name        = "Turnip catapult"
    override val desc        = "Turnips cause splash damage"
    override val unlock_wave = 10
    override val buy_cost    = 2500
    override val sell_cost   = 2000
}
