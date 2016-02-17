
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

object Tower
{
  val tower_graphic = ImageIO.read(new File(getClass().getResource("/towers/base_tower.png").getPath()))
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
  val throw_speed    = 10.0
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

object QuickTower
{
  val tower_graphic = ImageIO.read(new File(getClass().getResource("/towers/quick_tower.png").getPath()))
}
class QuickTower(pos:Waypoint) extends Tower(pos) {
  import QuickTower._
  override val range          = 2
  override val throw_cooldown = 0.5
  override val throw_speed    = 20.0
  override val damages        = 4
  override val buy_cost       = 15
  override val sell_cost      = 8

  override def clone_at(newpos: Waypoint): QuickTower = {
    return new QuickTower(newpos)
  }

  override def graphic(): BufferedImage = {
    return tower_graphic
  }
}

object HeavyTower
{
  val tower_graphic = ImageIO.read(new File(getClass().getResource("/towers/heavy_tower.png").getPath()))
}

class HeavyTower(pos:Waypoint) extends Tower(pos) {
  import HeavyTower._
  override val range       = 4
  override val throw_speed = 15.0
  override val damages     = 9
  override val buy_cost    = 12
  override val sell_cost   = 7

  override def clone_at(newpos: Waypoint): HeavyTower = {
    return new HeavyTower(newpos)
  }

  override def graphic(): BufferedImage = {
    return tower_graphic
  }
}

/* AOE Tower (spinning scarecrow) */
class ScarecrowTower(pos:Waypoint) extends Tower(pos) {
  override val range     = 4
  override val damages   = 4
  override val buy_cost  = 15
  override val sell_cost = 8

  override def clone_at(newpos: Waypoint): ScarecrowTower = {
    return new ScarecrowTower(newpos)
  }

  override def attack(): Unit = {
    val bunnies : ListBuffer[Bunny] = Controller.bunnies.filter( in_range )
    if (bunnies.length == 0) {
      cooldown = throw_cooldown
      bunnies.map( fire_at )
    }
  }
}
