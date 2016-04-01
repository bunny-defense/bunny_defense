
package gui

import collection.mutable.ListBuffer
import util.Random

import java.awt.Graphics2D
import java.awt.AlphaComposite

import runtime.TowerDefense

import game_mechanics.path.{Waypoint,CellPos}

object RaygunAnimation
{
    val rng = new Random
}

class RaygunAnimation(tower_pos: CellPos) extends Animatable
{
    import RaygunAnimation._
    val origin         = tower_pos.toDouble
    val duration       = 10.0
    timer = duration
    val size           = MapPanel.cellsize.toDouble
    val particle_delay = 0.25
    var particles      = new ListBuffer[Waypoint]

    override def draw(g: Graphics2D): Unit = {
        val interp = 1.0 - timer / duration
        g.setColor( Colors.black )
        g.setComposite(
            AlphaComposite.getInstance( AlphaComposite.SRC_OVER, interp.toFloat ) )
        g.fillRect( 0, 0,
            (TowerDefense.map_panel.map.width * size).toInt,
            (TowerDefense.map_panel.map.height * size).toInt )
        g.setComposite(
            AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 1f ) )
        while( particles.length < ((duration - timer) / particle_delay).toInt &&
            particles.length < 10)
        {
            val r = 2 * rng.nextDouble * Math.PI
            val amp = 4 + rng.nextDouble * 2
            val particle = new Waypoint( amp * Math.cos(r) , amp * Math.sin(r) )
            particles += particle
        }
        g.setColor( Colors.transparent_white )
        for( i <- 0 until particles.length )
        {
            val a = origin +
                particles(i) *
                    Math.pow((timer + i * particle_delay) / duration, 4) +
                        new Waypoint( 0.5, 0.5 )
            val b = origin +
                particles(i) *
                    Math.pow((timer + i * particle_delay) / duration, 2) +
                        new Waypoint( 0.5, 0.5 )
            g.drawLine(
                (a.x * size).toInt, (a.y * size).toInt,
                (b.x * size).toInt, (b.y * size).toInt)
        }
        val orbsize = (1.0 - timer / duration) * size
        val o = (origin + new Waypoint(0.5,0.5)) * size + (new Waypoint(0.5,0.5)) * (1 - orbsize)
        g.setColor( Colors.white )
        g.fillOval( o.x.toInt, o.y.toInt, orbsize.toInt, orbsize.toInt )
    }
}