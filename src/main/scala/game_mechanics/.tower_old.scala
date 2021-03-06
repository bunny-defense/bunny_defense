
package game_mechanics

import runtime.Controller
import runtime.Spawner
import path._ // Really necessary ?
import game_mechanics._
import scala.collection.mutable._
import Math._

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

trait TowerType
{
    val tower_graphic  =
        ImageIO.read(
            new File(getClass().getResource("/towers/base_tower.png").getPath()))
    val throw_graphic  =
        ImageIO.read(
            new File(
                getClass().getResource("/projectiles/carrot1.png").getPath()))
    val size           = 1      /* Size in tiles */
    var damage         = 5      /* Damage dealt to bunnies */
    var range          = 5      /* Range in tiles */
    val aoe_radius     = 0      /*  Not sure what that really means ? */
    val throw_speed    = 10.0   /* Speed of the shot projectile */
    val throw_cooldown = 1.0    /* Cooldown time in seconds */
    val buy_cost       = 50     /* Gold needed to buy one */
    val sell_cost      = 25     /* Gold earned when sold */
}

object BaseTower extends TowerType

object QuickTower extends TowerType
{
    override val tower_graphic  =
        ImageIO.read(
            new File(getClass().getResource("/towers/quick_tower.png").getPath()))
    override val throw_graphic  =
        ImageIO.read(
            new File(getClass().getResource("/projectiles/leaf.png").getPath()))
    range          = 3
    override val throw_cooldown = 0.5
    override val throw_speed    = 20.0
    damage         = 4
    override val buy_cost       = 75
    override val sell_cost      = 35
}

object HeavyTower extends TowerType
{
    override val tower_graphic =
        ImageIO.read(
            new File(getClass().getResource("/towers/tank.png").getPath()))
    range         = 4
    override val throw_speed   = 15.0
    damage        = 9
    override val buy_cost      = 150
    override val sell_cost     = 75
}


/* AOE Tower (spinning scarecrow) */
object ScarecrowTower extends TowerType
{
    override val tower_graphic =
        ImageIO.read(
            new File(getClass().getResource("/towers/heavy_tower.png").getPath()))
    range     = 4
    damage    = 6
    override val buy_cost  = 150
    override val sell_cost = 80
    override val aoe_radius = 10
}

/* Human commando in a fishtank who uses phychic powers to continuouly hurt nearby rabbits (metal as hell) */
object Roberto extends TowerType
{
    override val tower_graphic =
        ImageIO.read(
            new File(getClass().getResource("/towers/roberto.png").getPath()))
    override val throw_graphic =
        ImageIO.read(
            new File(getClass().getResource("/blank.png").getPath()))
    range     = 6
    damage    = 1
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
    /* The tower keeps a selected target until it goes out of range or is changed */
    var current_target : Option[Bunny] = None
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

    def closest_to_p(p: Waypoint): Option[Bunny] = {
        /* Returns the bunny that is the closest to a given point p */
        def distance_comp(x: Bunny,y: Bunny) = (x.pos-p).norm < (y.pos-p).norm
        val bunnies = Controller.bunnies.filter(in_range).sortWith(distance_comp)
        if (bunnies.isEmpty) {return None}
        else {return Some(bunnies.head)}
    }

    /* Returns the target of the tower */
    def get_target(): Option[Bunny] = {
        val p = Spawner.bunnyend
        if( current_target == None
            || !in_range(current_target.get)
            || !(current_target == closest_to_p(p))
            || current_target.get.hp <= 0 )
        {
            def distance_comp(x: Bunny,y: Bunny) = (x.pos-p).norm < (y.pos-p).norm
            val bunnies = Controller.bunnies.filter(in_range).sortWith(distance_comp)
            if( !bunnies.isEmpty )
                current_target = Some(bunnies.head)
            else
                current_target = None
        }
        return current_target
    }

    /* Self descriptive */
    def fire_at(bunny: Bunny): Unit = {
        var throw_carrot    = new Throw(bunny, this.pos.toDouble, tower_type)
        throw_carrot.speed  = tower_type.throw_speed
        throw_carrot.damage = this.damage
        throw_carrot.AOE    = tower_type.aoe_radius
        Controller += throw_carrot
    }

    /* Creates a Throw object, with the characteristics of the tower */
    def attack(): Unit = if (tower_type.aoe_radius == 0)
    { 
        {
            if( get_target() == None )
                return
                    cooldown = tower_type.throw_cooldown /* Resetting the cooldown */
            fire_at( current_target.get )
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


