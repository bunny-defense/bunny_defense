
package game_mechanics.tower

import runtime.Controller
import runtime.Spawner
import game_mechanics._
import game_mechanics.path._
import scala.collection.mutable._
import Math._

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/* Tower superclass from which evey special tower is derived */
class Tower(tower_type : TowerType, pos0 : CellPos) {
    val pos            = pos0
    /* Cooldown counter */
    var cooldown       = 0.0
    val towertype      = tower_type
    var damage         = tower_type.damage
    var range          = tower_type.range
    var upgrades : UpgradeTree = BaseTowerUpgrades

    // ==============================
    //  UPGRADE MECHANICS
    // ==============================
    tower_type match
    {
        case BaseTower  => upgrades = BaseTowerUpgrades
        case QuickTower => upgrades = QuickTowerUpgrades
        case _          => upgrades = BaseTowerUpgrades
    }

    // ==============================
    //  FIRING MECHANICS
    // ==============================

    /* Returns whether the bunny is in the range of the tower or not */
    def in_range(bunny: Bunny): Boolean = {
        return ((bunny.pos - pos).norm <= this.range)
    }

    def closest_to(p: Waypoint): Option[Bunny] = {
        /* Returns the bunny that is the closest to a given point p */
        def distance_comp(x: Bunny,y: Bunny) =
            (x.pos-p).norm < (y.pos-p).norm
        val bunnies =
            Controller.bunnies
                .filter(_.alive)
                .filter(in_range)
                .sortWith(distance_comp)
        if (bunnies.isEmpty) {return None}
        else {return Some(bunnies.head)}
    }

    /* Returns the target of the tower */
    def get_targets(): Option[Bunny] = {
        return closest_to( Spawner.bunnyend )
    }

    /* Self descriptive */
    val fire_at : Bunny => Unit = tower_type.fire_from( this )

    /* Creates a Projectile object, with the characteristics of the tower */
    def attack(): Unit = if (tower_type.aoe_radius == 0)
    {
        {
            val target = get_targets()
            if( target == None )
                return
                    cooldown = tower_type.throw_cooldown /* Resetting the cooldown */
            fire_at( target.get )
        }
    }
    else
    {
        {
            val bunnies : ListBuffer[Bunny] = Controller.bunnies.filter( in_range )
            if (bunnies.length != 0) {
                cooldown = tower_type.throw_cooldown
                bunnies.map( fire_at )
            }
        }
    }

    // ==============================
    //  UPDATING LOGIC
    // ==============================

    /* Updates the tower */
    def update(dt: Double): Unit = {
        if( cooldown <= 0 )
            attack()
        else
            cooldown -= dt
    }

    // ==============================
    //  GETTERS
    // ==============================

    def throw_speed(): Double = {
        return tower_type.throw_speed
    }

    def buy_cost() : Int = {
        return tower_type.buy_cost
    }

    def sell_cost() : Int = {
        return tower_type.sell_cost
    }

    def graphic(): BufferedImage = {
        return tower_type.tower_graphic
    }

    def clone_at(newpos: CellPos): Tower = {
        return new Tower(tower_type, newpos)
    }
}


