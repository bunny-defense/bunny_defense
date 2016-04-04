
package gui.animations

import collection.mutable.ListBuffer
import util.Random

import java.awt.Graphics2D

import game_mechanics.path.Waypoint
import gui._

class SmokeAnimation(origin: Waypoint) extends Animatable
{
    /** This is the smoke animation the ninja bunnies (specopsbunnies) do
     *  when teleporting */

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
