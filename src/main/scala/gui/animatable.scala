
package gui

import java.awt.Graphics2D

import game_mechanics.path.Waypoint
import runtime.Controller

/* Creates nice animations when bunnies die */

abstract class Animatable
{
  def update(dt: Double): Unit
  def draw(g: Graphics2D): Unit
}

class GoldAnimation(amount: Int,origin: Waypoint) extends Animatable
{
  var pos    = origin
  val target = origin + new Waypoint(0,-1)
  var timer  = 1.0


  def update(dt: Double): Unit = {
    timer -= dt
    pos = origin * timer + target * (1 - timer)
    if( timer <= 0 )
      Controller -= this
  }

  def draw(g: Graphics2D): Unit = {
    g.drawString( "+" + amount.toString + " Gold",
      pos.x.toFloat * MapPanel.cellsize,
      pos.y.toFloat * MapPanel.cellsize )
  }
}
