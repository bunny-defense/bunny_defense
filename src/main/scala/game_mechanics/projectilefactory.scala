
package game_mechanics

import runtime.GameState
import game_mechanics.tower.TowerType
import game_mechanics.path.Waypoint

object ProjectileFactory
{
    /** A factory that creates projectiles */
    val BASE_PROJECTILE   = 0
    val SPLASH_PROJECTILE = 1

    def create(
        projectile_type: Int,
        owner: Player,
        targetpos: Waypoint,
        origin: Waypoint,
        firing_tower: TowerType,
        gamestate: GameState ) : Projectile = {
            projectile_type match {
                case BASE_PROJECTILE =>
                    new Projectile(owner,
                        targetpos, origin, firing_tower,
                        gamestate)
                case SPLASH_PROJECTILE =>
                    new SplashProjectile(owner,
                        targetpos, origin, firing_tower,
                        gamestate)
                case _ => throw new Exception( "Not a tower type" )
            }
    }
}
