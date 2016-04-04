
package game_mechanics.tower

import collection.mutable.ListBuffer

import game_mechanics.{Projectile,ProjectileFactory}
import game_mechanics.bunny.Bunny
import game_mechanics.path.Waypoint
import runtime.{Spawner,Controller}
import gui.MuzzleflashAnimation

class ShooterTower(projectile_type : Int) extends TowerType
{
    override def attack_from(tower : Tower): () => Boolean = {
        def in_range(bunny : Bunny) : Boolean = {
            return (bunny.pos - tower.pos).norm <= tower.range
        }
        def fire_at(bunny: Bunny): Unit = {
            Controller += new MuzzleflashAnimation(tower.pos.toDouble)
            val target_pos = bunny.pos + (Waypoint.random() * 2 - new Waypoint( 1, 1 )) * spread
            var throw_carrot    = ProjectileFactory.create(
                projectile_type, target_pos, tower.pos.toDouble, this)
            throw_carrot.speed  = throw_speed
            throw_carrot.damage = tower.damage
            Controller += throw_carrot
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

