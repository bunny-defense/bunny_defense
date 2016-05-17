
package game_mechanics.tower

import collection.mutable.ListBuffer

import game_mechanics.{Projectile,ProjectileFactory}
import game_mechanics.bunny.Bunny
import game_mechanics.path.Waypoint
import runtime.TowerDefense
import runtime.GameState
import gui.animations.MuzzleflashAnimation

class ShooterTower(projectile_type : Int) extends TowerType
{
    /**
     * The class that defines the methods of all shooting towers
     * @param projectile_type : The type of projectile sent by the tower
     */
    override def attack_from(tower : Tower, gamestate: GameState): () => Boolean = {
        def in_range(bunny : Bunny) : Boolean = {
            return ((bunny.pos - tower.pos).norm <= tower.range &&
                     tower.owner != bunny.owner)
        }

        def fire_at(bunny: Bunny): Unit = {
            gamestate.tower_fire_strategy(tower)
            // gamestate += new MuzzleflashAnimation(tower.pos.toDouble)
            val target_pos = bunny.pos + (Waypoint.random() * 2 - new Waypoint( 1, 1 )) * spread
            var throw_carrot    = ProjectileFactory.create(
                projectile_type,
                tower.owner,
                target_pos,
                tower.pos.toDouble, this,
                gamestate)
            throw_carrot.speed  = throw_speed
            throw_carrot.damage = tower.damage
            gamestate += throw_carrot
        }

        def closest_to( point : Waypoint ) : Option[Bunny] = {

            def distance_comp( x : Bunny, y : Bunny ) =
                (x.pos - point).norm < (y.pos - point).norm

            val bunnies =
                gamestate.bunnies
                    .filter(_.alive)
                    .filter(in_range)
                    .sortWith(distance_comp)
            if( bunnies.isEmpty )
                return None
            else
                return Some(bunnies.head)
        }

        def get_target() : Option[Bunny] = {
            return closest_to( tower.owner.base.toDouble )
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

