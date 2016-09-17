
package gui.animations

import java.awt.Graphics2D
import java.awt.AlphaComposite

import collection.mutable.ListBuffer
import util.Random

import runtime.GuiGameState
import game_mechanics.path.{Waypoint,CellPos}
import gui._



object SnowAnimation
{
    val rng = new Random()
}

class SnowAnimation(pos : CellPos, radius : Double, gamestate: GuiGameState)
extends Animatable(gamestate)
{
    /** This is the snow animation played around slowing towers */
    import SnowAnimation._
    val size            = MapPanel.cellsize.toDouble
    val origin          = (pos.toDouble + new Waypoint(0.5,0.5)) * size
    val duration        = 7.0
    timer               = duration
    val particle_amount = 30
    val particles       = new ListBuffer[Waypoint]()
    val fall_distance   = 20

    for( i <- 0 until particle_amount )
    {
        // Uniform disk distribution
        val r      = rng.nextDouble
        val theta  = rng.nextDouble * 2 * Math.PI
        val x      = Math.sqrt( r ) * Math.cos( theta ) * radius * size
        val y      = Math.sqrt( r ) * Math.sin( theta ) * radius * size
        particles += new Waypoint( x + origin.x, y + origin.y - fall_distance )
    }

    override def draw(g: Graphics2D): Unit = {
        g.setColor( Colors.white )
        val alpha = (timer / duration).toFloat
        g.setComposite(
            AlphaComposite.getInstance( AlphaComposite.SRC_OVER, alpha ) )
        val movement = (1.0 - timer / duration) * fall_distance
        for( particle <- particles ) {
            g.drawRect( particle.x.toInt, particle.y.toInt + movement.toInt,
                1, 1 )
        }
    }
}
