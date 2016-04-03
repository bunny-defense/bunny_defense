
package game_mechanics

import game_mechanics.tower.TowerType
import game_mechanics.path.Waypoint

object ProjectileFactory
{
    val BASE_PROJECTILE   = 0
    val SPLASH_PROJECTILE = 1

    def create(
        projectile_type : Int,
        targetpos : Waypoint,
        origin : Waypoint,
        firing_tower : TowerType ) : Projectile = {
            projectile_type match {
                case BASE_PROJECTILE =>
                    new Projectile(targetpos, origin, firing_tower )
                case SPLASH_PROJECTILE =>
                    new SplashProjectile(targetpos, origin, firing_tower)
                case _ => throw new Exception( "Not a tower type" )
            }
    }
}
