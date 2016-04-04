
package gui

import collection.mutable.ListBuffer
import util.Random

import java.awt.Graphics2D

import game_mechanics.path.Waypoint

class SmokeAnimation(origin: Waypoint) extends Animatable
{
    val duration = 2.0
    val rng      = new Random
    timer        = duration

    val particles = new ListBuffer[Waypoint]

    for( i <- 0 until 30 )
    {
        val theta = rng.nextDouble * 2 * Math.PI
        particles += new Waypoint( Math.cos( theta ), Math.sin( theta ) )
    }

    override def draw(g: Graphics2D) : Unit = {
        val interp = 1 - timer / duration
        g.setColor( Colors.lightGrey )
        for( particle <- particles )
        {
            val pos = (origin + particle * interp * 1.5) * MapPanel.cellsize
            g.fillOval(
                pos.x.toInt, pos.y.toInt,
                (MapPanel.cellsize * (1-interp)).toInt,
                (MapPanel.cellsize * (1-interp)).toInt )
        }
    }
}
