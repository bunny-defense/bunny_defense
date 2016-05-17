
package game_mechanics.tower

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import runtime.Controller
import game_mechanics._
import game_mechanics.bunny.Bunny
import game_mechanics.path.Waypoint

/* AOE Tower (spinning scarecrow) */
object ScarecrowTower extends TowerType
{
    override val name = "Scarecrow"
    override val desc = "Shoots carrots at all nearby enemies"
    upgrades          = Some(FireRateUpgrade)
    override val tower_graphic =
        ImageIO.read(
            new File(
                getClass().getResource("/towers/scarecrow.png").getPath()))
    base_range                = 4
    range                     = 4
    base_damage               = 6
    damage                    = 6
    override val price        = 1500
    sell_cost                 = 1200
    override val unlock_wave  = 10


    override def attack_from(tower : Tower) : () => Boolean = {
        def in_range(bunny : Bunny) : Boolean = {
            return (bunny.pos - tower.pos).norm <= tower.range
        }
        def fire_at(bunny : Bunny) : Unit = {
            val target_pos = bunny.pos +
                (Waypoint.random() * 2 - new Waypoint( 1, 1 )) * spread
            val projectile = new Projectile( target_pos, tower.pos.toDouble, this)
            projectile.speed = throw_speed
            projectile.damage = tower.damage
            Controller += projectile
        }
        def attack(): Boolean = {
            val bunnies = Controller.bunnies.filter( in_range )
            if( !bunnies.isEmpty )
            {
                bunnies.map( fire_at )
                return true
            }
            return false
        }
        attack
    }

}
