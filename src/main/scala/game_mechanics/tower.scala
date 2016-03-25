
package game_mechanics

import runtime.Controller
import runtime.Spawner
import path._ // Really necessary ?
import game_mechanics._
import game_mechanics.path.CellPos
import scala.collection.mutable._
import Math._

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

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
    val damage         = 5      /* Damage dealt to bunnies */
    val range          = 5      /* Range in tiles */
    val aoe_radius     = 0      /* Not sure what that really means ? */
    val throw_speed    = 10.0   /* Speed of the shot projectile */
    val throw_cooldown = 1.0    /* Cooldown time in seconds */
    val buy_cost       = 50     /* Gold needed to buy one */
    val sell_cost      = 25     /* Gold earned when sold */

    /* Self descriptive */
    def fire_from(pos : CellPos)(bunny: Bunny): Unit = {
        var throw_carrot    = new Throw(bunny, pos.toDouble, this)
        throw_carrot.speed  = throw_speed
        throw_carrot.damage = damage
        throw_carrot.AOE    = aoe_radius
        Controller += throw_carrot
    }

    def get_targets(in_range : ListBuffer[Bunny]) : ListBuffer[Bunny] = {
        return new ListBuffer[Bunny]
    }
}

object BaseTower extends TowerType
{
    def closest_to(bunnies : ListBuffer[Bunny], p: Waypoint): Option[Bunny] = {
        /* Returns the bunny that is the closest to a given point p */
        def distance_comp(x: Bunny,y: Bunny) =
            (x.pos-p).norm < (y.pos-p).norm
        val sorted_bunnies = bunnies.sortWith(distance_comp)
        if (sorted_bunnies.isEmpty) {return None}
        else {return Some(sorted_bunnies.head)}
    }

    override def get_targets(in_range : ListBuffer[Bunny])
        : ListBuffer[Bunny] = {
        closest_to( in_range, Spawner.bunnyend ) match {
            case None => new ListBuffer[Bunny]()
            case Some(bunny) => {
                val list = new ListBuffer[Bunny]
                list += bunny
                return list
            }
        }
    }
}


object QuickTower extends TowerType
{
    override val tower_graphic  =
        ImageIO.read(
            new File(
                getClass().getResource("/towers/quick_tower.png").getPath()))
    override val range          = 3
    override val throw_cooldown = 0.5
    override val throw_speed    = 20.0
    override val damage         = 4
    override val buy_cost       = 75
    override val sell_cost      = 35
}

object HeavyTower extends TowerType
{
    override val tower_graphic =
        ImageIO.read(
            new File(
                getClass().getResource("/towers/tank.png").getPath()))
    override val range         = 4
    override val throw_speed   = 15.0
    override val damage        = 9
    override val buy_cost      = 150
    override val sell_cost     = 75
}


/* AOE Tower (spinning scarecrow) */
object ScarecrowTower extends TowerType
{
    override val tower_graphic =
        ImageIO.read(
            new File(
                getClass().getResource("/towers/heavy_tower.png").getPath()))
    override val range     = 4
    override val damage    = 6
    override val buy_cost  = 150
    override val sell_cost = 80
    override val aoe_radius = 10
}

/* Human commando in a fishtank who uses phychic powers to continuouly hurt nearby rabbits (metal as hell) */
object Roberto extends TowerType
{
    override val tower_graphic =
        ImageIO.read(
            new File(
                getClass().getResource("/towers/roberto.png").getPath()))
    override val throw_graphic =
        ImageIO.read(
            new File(
                getClass().getResource("/blank.png").getPath()))
    override val range     = 6
    override val damage    = 1
    override val buy_cost  = 300
    override val sell_cost = 150
    override val throw_speed = 100.0
    override val throw_cooldown = 0.05
}

/* Tower superclass from which evey special tower is derived */
class Tower(tower_type : TowerType, pos0 : CellPos) {
    val pos            = pos0
    /* Cooldown counter */
    var cooldown       = 0.0
    val towertype      = tower_type

    // ==============================
    //  FIRING MECHANICS
    // ==============================

    /* Returns whether the bunny is in the range of the tower or not */
    def in_range(bunny: Bunny): Boolean = {
        return ((bunny.pos - pos).norm <= tower_type.range)
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
    val fire_at : Bunny => Unit = tower_type.fire_from( this.pos )

    /* Creates a Throw object, with the characteristics of the tower */
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

    def damage() : Int = {
        return tower_type.damage
    }

    def range() : Int = {
        return tower_type.range
    }

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


