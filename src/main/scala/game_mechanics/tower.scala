
package game_mechanics

import runtime.Controller
import path._ // Really necessary ?
import game_mechanics._
import scala.collection.mutable._
import Math._

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object Tower
{
  val tower_graphic = ImageIO.read(new File(getClass().getResource("/towers/tower.png").getPath()))
}

/* Tower superclass from which evey special tower is derived */
class Tower(pos0:Waypoint) {
  import Tower._
  val pos            = pos0
  val size           = 1
  val damages        = 5
  val range          = 5
  val aoe_radius     = 0
  /* Speed of the shot projectile */
  val throw_speed    = 1.0
  /* Cooldown time in seconds */
  val throw_cooldown = 1.0
  /* Cooldown counter */
  var cooldown       = 0.0
  val buy_cost       = 10
  val sell_cost      = 5
  /* The tower keeps a selected target until it goes out of range */
  var current_target : Option[Bunny] = None

  /* Returns whether or not the bunny is in the range of the tower */
  def in_range(bunny: Bunny): Boolean = {
    return ((bunny.pos - pos).norm <= range)
  }

  /* Returns the target of the tower */
  def get_target(): Option[Bunny] = {
    if( current_target == None
      || !in_range(current_target.get)
      || current_target.get.hp <= 0 )
    {
      val bunnies = Controller.bunnies.filter( in_range )
      if( !bunnies.isEmpty )
        current_target = Some(bunnies.head)
      else
        current_target = None
    }
    return current_target
  }

  /* Self descriptive */
  def fire_at(bunny: Bunny): Unit = {
    var throw_carrot    = new Throw(bunny,this.pos.clone())
    throw_carrot.speed  = throw_speed
    throw_carrot.damage = damages
    throw_carrot.AOE    = aoe_radius
    Controller += throw_carrot
  }

  /* Creates a Throw object, with the characteristics of the tower */
  def attack(): Unit = {
    if( get_target() == None )
      return
    cooldown = throw_cooldown /* Resetting the cooldown */
    fire_at( current_target.get )
  }

  /* Updates the tower */
  def update(dt: Double): Unit = {
    if( cooldown <= 0 )
      attack()
    else
      cooldown -= dt
  }

  def graphic(): BufferedImage = {
    return tower_graphic
  }

  def clone_at(newpos: Waypoint): Tower = {
    return new Tower(newpos)
  }
}

class QuickTower(pos:Waypoint) extends Tower(pos) {
  override val range       = 75
  override val throw_speed = 5.0
  override val damages     = 4
  override val buy_cost    = 15
  override val sell_cost   = 8
}

class HeavyTower(pos:Waypoint) extends Tower(pos) {
  override val range       = 75
  override val throw_speed = 15.0
  override val damages     = 9
  override val buy_cost    = 12
  override val sell_cost   = 7
}

/* AOE Tower (spinning scarecrow) */
class ScarecrowTower(pos:Waypoint) extends Tower(pos) {
  override val range     = 10
  override val damages   = 4
  override val buy_cost  = 15
  override val sell_cost = 8

  override def attack(): Unit = {
    val bunnies : ListBuffer[Bunny] = Controller.bunnies.filter( in_range )
    if (bunnies.length == 0) {
      cooldown = throw_cooldown
      bunnies.map( fire_at )
    }
  }
}
