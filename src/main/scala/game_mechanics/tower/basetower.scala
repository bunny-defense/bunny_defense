
package game_mechanics.tower

import game_mechanics._

object BaseTower extends ShooterTower(ProjectileFactory.BASE_PROJECTILE)
{
    override val name = "Basic tower"
    override val desc = "Shoots carrots at a decent rate"
    upgrades = Some(DamageUpgrade)
}
