
package game_mechanics

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import runtime.Controller
import game_mechanics.path._
import gui.GoldAnimation

object Bunny
{
  val bunny_graphic = ImageIO.read(new File(getClass().getResource("/mobs/bunny1.png").getPath()))
}

/* Bunny superclass from which every ennemy is derived. */
class Bunny(path0: Progress) {
  import Bunny._
  var hp              = 10.0
  var pos : Waypoint  = path0.path.waypoints(0)
  var shield          = 1.0
  var speed           = 1.0
  var path            = path0
  /* Amount of gold earned when killed */
  val reward          = 10
  /* Damage done to the player when core reached */
  val damage          = 1

  /* Prototype design pattern */
  def copy(): Bunny = {
    return new Bunny( new Progress( path.path ) )
  }

  def remove_hp(dmg: Double): Unit = { this.hp -= dmg
  }

  /* Moves the bunny along the path */
  def move(dt: Double): Unit = {
    path.move(dt*this.speed)
    pos = path.get_position
  }

  def update(dt: Double): Unit = {
    if( hp <= 0 )
    {
      Controller += new GoldAnimation(reward, pos.clone())
      Player.add_gold( reward )
      Controller -= this
    }
    move(dt)
    if( path.reached )
    {
      Player.remove_hp( damage )
      Controller -= this
      println( "Bunny reached core" )
    }
  }

  def graphic(): BufferedImage = {
    return bunny_graphic
  }
}

/* Large and tough but slow bunny */
class Heavy_Bunny(path0: Progress) extends Bunny(path0) {
  this.hp     = 20
  this.shield = 1.5
  this.speed  = 0.5
  override val reward = 15
}

/* Fast "Bunny" */
class Hare(path0: Progress) extends Bunny(path0: Progress)
{
  hp     = 5
  shield = 0.0
  speed  = 2.0
}

/* The boss! */
class Otter(path0: Progress) extends Bunny(path0) {
  this.hp     = 1000
  this.shield = 1.5
  this.speed  = 0.5
  override val damage = 5
  override val reward = 100
}

object Golden_Bunny
{
  val bunny_graphic = ImageIO.read(new File(getClass().getResource("/mobs/goldenbunny.png").getPath()))
}

class Golden_Bunny(path0: Progress) extends Bunny(path0) {
  this.speed = 2.0
  this.hp = 20
  override val reward = 1000
  override def copy(): Golden_Bunny = {
    return new Golden_Bunny( new Progress( path.path ) )
  }
}

