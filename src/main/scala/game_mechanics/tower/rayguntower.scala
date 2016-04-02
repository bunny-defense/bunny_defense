
package game_mechanics.tower

import runtime.{Controller,Spawner}

import gui.RaygunAnimation

import game_mechanics.bunny.Bunny
import game_mechanics.path.Waypoint

object RaygunTower extends TowerType
{
    override val throw_cooldown = RaygunAnimation.duration

    override def attack_from(tower : Tower) : () => Boolean = {
        def in_range(bunny : Bunny) : Boolean = {
            return (bunny.pos - tower.pos).norm <= tower.range
        }
        def fire_at(bunny : Bunny) : Unit = {
            Controller += new RaygunAnimation( tower.pos )
        }
        def closest_to( point : Waypoint ) : Option[Bunny] = {
            def distance_comp( x : Bunny, y : Bunny ) =
                (x.pos - point).norm < (y.pos - point).norm
            val bunnies =
                Controller.bunnies
                    .filter(_.alive)
                    .filter(in_range)
                    .sortWith(distance_comp)
            if( bunnies.isEmpty )
                return None
            else
                return Some(bunnies.head)
        }
        def get_target() : Option[Bunny] = {
            return closest_to( Spawner.bunnyend.toDouble )
        }
        def attack(): Boolean = {
            val target = get_target()
            if( target == None )
                return false
            fire_at( target.get )
            return true
        }
        return attack
    }
}
