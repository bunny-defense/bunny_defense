
package game_mechanics.tower

import collection.mutable.ListBuffer

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import runtime.Controller
import game_mechanics.{Projectile,Bunny}
import game_mechanics.path._


trait TowerType
{
    val tower_graphic  =
        ImageIO.read(
            new File(
                getClass().getResource("/towers/base_tower.png").getPath()))
    val throw_graphic  =
        ImageIO.read(
            new File(
                getClass().getResource("/projectiles/carrot1.png").getPath()))
    val size           = 1      /* Size in tiles */
    var damage         = 5      /* Damage dealt to bunnies */
    var range          = 5      /* Range in tiles */
    val spread         = 0.0    /* Amount of bullet spread */
    val aoe_radius     = 0      /* Not sure what that really means ? */
    val throw_speed    = 10.0   /* Speed of the shot projectile */
    val throw_cooldown = 1.0    /* Cooldown time in seconds */
    val buy_cost       = 50     /* Gold needed to buy one */
    val sell_cost      = 25     /* Gold earned when sold */

    var amount         = 0

    /* Self descriptive */
    def fire_from(tower : Tower)(bunny: Bunny): Unit = {
        val target_pos = bunny.pos + (Waypoint.random() * 2 - new Waypoint( 1, 1 )) * spread
        var throw_carrot    = new Projectile(target_pos, tower.pos.toDouble, this)
        throw_carrot.speed  = throw_speed
        throw_carrot.damage = tower.damage
        throw_carrot.AOE    = aoe_radius
        Controller += throw_carrot
    }

    def get_targets(in_range : ListBuffer[Bunny]) : ListBuffer[Bunny] = {
        return new ListBuffer[Bunny]
    }
}

