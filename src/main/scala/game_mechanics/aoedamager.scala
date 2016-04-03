
package game_mechanics

import game_mechanics.path.Waypoint
import runtime.Controller

class AOEDamager(pos: Waypoint, radius: Int, damage: Int) extends Updatable
{
    override def update(dt: Double): Unit = {
        val targets = Controller.bunnies.filter(
            bunny => pos.distance_to(bunny.pos) < radius )
        for( target <- targets )
        {
            target.remove_hp( damage * dt )
        }
    }
}
