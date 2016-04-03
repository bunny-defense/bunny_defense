
package game_mechanics

import runtime.Controller
import game_mechanics.path.Waypoint
import game_mechanics.tower.TowerType
import game_mechanics.bunny.Bunny

/* This projectile causes splash damage */
class SplashProjectile(
    targetpos: Waypoint,
    origin: Waypoint,
    firing_tower: TowerType)
extends Projectile(
    targetpos,
    origin,
    firing_tower)
{
    val radius = 5
    damage = 3
    override def on_hit(target : Option[Bunny]): Unit = {
        val targets = Controller.bunnies
            .filter( bunny => pos.distance_to( bunny.pos ) < radius )
        targets.foreach( _.remove_hp( damage ) )
        Controller -= this
    }
}
