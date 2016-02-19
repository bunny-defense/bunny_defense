
package game_mechanics

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import runtime.Controller
import game_mechanics.path._
import gui.GoldAnimation

trait BunnyType
{
  val bunny_graphic =
    ImageIO.read(
      new File(getClass().getResource("/mobs/bunny1.png").getPath()))
  val initial_hp    = 10.0  /* Initial amount of HP */
  val shield        = 1.0   /* Damage dampening */
  val speed         = 1.0   /* Speed of the bunny in tiles per second */
  val reward        = 10    /* Amount of gold earned when killed */
  val damage        = 1     /* Damage done to the player when core reached */
}

object NormalBunny extends BunnyType

/* Large and tough but slow bunny */
object HeavyBunny extends BunnyType
{
  override val initial_hp = 20.0
  override val shield     = 1.5
  override val speed      = 0.5
  override val reward     = 15
}

/* Fast "Bunny" */
object Hare extends BunnyType
{
  override val initial_hp = 5.0
  override val shield     = 0.0
  override val speed      = 2.0
}

/* A boss! */
object Otter extends BunnyType
{
  override val initial_hp = 1000.0
  override val shield     = 1.5
  override val speed      = 0.5
  override val damage     = 5
  override val reward     = 100
}

/* Rare golden bunny worth a lot of money */
object GoldenBunny extends BunnyType
{
  override val bunny_graphic =
    ImageIO.read(new File(
      getClass().getResource("/mobs/goldenbunny.png").getPath()))
  override val initial_hp    = 20.0
  override val speed         = 2.0
  override val reward        = 1000
}

/* Bunny superclass from which every ennemy is derived. */
class Bunny(bunny_type: BunnyType,path0: Progress) {
  var hp              = bunny_type.initial_hp
  var pos : Waypoint  = path0.path.waypoints(0)
  var path            = path0

  /* Prototype design pattern */
  def copy(): Bunny = {
    return new Bunny( bunny_type, new Progress( path.path ) )
  }

  def remove_hp(dmg: Double): Unit = {
    this.hp -= dmg
  }

  /* Moves the bunny along the path */
  def move(dt: Double): Unit = {
    path.move( dt * bunny_type.speed )
    pos = path.get_position
  }

  def update(dt: Double): Unit = {
    if( hp <= 0 )
    {
      Controller += new GoldAnimation( bunny_type.reward, pos.clone() )
      Player.add_gold( bunny_type.reward )
      Controller -= this
    }
    move(dt)
    if( path.reached )
    {
      Player.remove_hp( bunny_type.damage )
      Controller -= this
      println( "Bunny reached core" )
    }
  }

  def initial_hp(): Double = {
    return bunny_type.initial_hp
  }

  def graphic(): BufferedImage = {
    return bunny_type.bunny_graphic
  }
}


