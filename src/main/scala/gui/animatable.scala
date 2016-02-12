
package gui

import java.awt.Graphics2D

import game_mechanics.path.Waypoint
import runtime.Controller

abstract class Animatable
{
  def update(dt: Double): Unit
  def draw(g: Graphics2D): Unit
}

class GoldAnimation(amount: Int,origin: Waypoint) extends Animatable
{
  var pos      = origin
  val target   = origin + new Waypoint(0,-1)
  var distance = 1.0


  def update(dt: Double): Unit = {
    distance -= dt
    pos = origin * distance + target * (1 - distance)
    if( distance <= 0 )
      Controller -= this
  }

  def draw(g: Graphics2D): Unit = {
    g.drawString( "+" + amount.toString + " Gold",
      pos.x.toFloat * MapPanel.cellsize,
      pos.y.toFloat * MapPanel.cellsize )
  }
}
