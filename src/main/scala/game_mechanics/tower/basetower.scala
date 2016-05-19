
package game_mechanics.tower

import game_mechanics.ProjectileFactory

object BaseTower extends ShooterTower(ProjectileFactory.BASE_PROJECTILE)
{
    override val name = "Basic tower"
    override val desc = "Shoots carrots at a decent rate"
    override def serialize() : Int = TowerType.BASE_TOWER
}
