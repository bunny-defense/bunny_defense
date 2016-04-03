
package game_mechanics.tower

import game_mechanics.ProjectileFactory

object SplashTower extends ShooterTower(ProjectileFactory.SPLASH_PROJECTILE)
{
    override val name         = "Turnip catapult"
    override val desc         = "Turnips cause splash damage"
    override val first_appear = 10
}
