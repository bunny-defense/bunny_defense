
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
  val size           = 1
  val damage         = 5
  val range          = 5
  val aoe_radius     = 0
  val throw_speed    = 10.0   /* Speed of the shot projectile */
  val throw_cooldown = 1.0   /* Cooldown time in seconds */
  val buy_cost       = 50
  val sell_cost      = 25
}

object BaseTower extends TowerType

object QuickTower extends TowerType
{
  override val tower_graphic  =
    ImageIO.read(
      new File(getClass().getResource("/towers/quick_tower.png").getPath()))
  override val range          = 2
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
      new File(getClass().getResource("/towers/heavy_tower.png").getPath()))
  override val range         = 4
  override val throw_speed   = 15.0
  override val damage        = 9
  override val buy_cost      = 150
  override val sell_cost     = 75
}

/* Tower superclass from which evey special tower is derived */
class Tower(tower_type : TowerType, pos0 : CellPos) {
  val pos            = pos0
  /* Cooldown counter */
  var cooldown       = 0.0
  /* The tower keeps a selected target until it goes out of range */
  var current_target : Option[Bunny] = None

  // ==============================
  //  FIRING MECHANICS
  // ==============================

  /* Returns whether or not the bunny is in the range of the tower */
  def in_range(bunny: Bunny): Boolean = {
    return ((bunny.pos - pos).norm <= tower_type.range)
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
    var throw_carrot    = new Throw(bunny,this.pos.toDouble)
    throw_carrot.speed  = tower_type.throw_speed
    throw_carrot.damage = tower_type.damage
    throw_carrot.AOE    = tower_type.aoe_radius
    Controller += throw_carrot
  }

  /* Creates a Throw object, with the characteristics of the tower */
  def attack(): Unit = {
    if( get_target() == None )
      return
    cooldown = tower_type.throw_cooldown /* Resetting the cooldown */
    fire_at( current_target.get )
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

/* AOE Tower (spinning scarecrow) */
object ScarecrowTower extends TowerType
{
  override val range     = 4
  override val damage    = 4
  override val buy_cost  = 150
  override val sell_cost = 8
}

class AOETower(tower_type : TowerType, pos : CellPos) extends Tower( tower_type, pos )
{
  override def attack(): Unit = {
    val bunnies : ListBuffer[Bunny] = Controller.bunnies.filter( in_range )
    if (bunnies.length == 0) {
      cooldown = tower_type.throw_cooldown
      bunnies.map( fire_at )
    }
  }
}


