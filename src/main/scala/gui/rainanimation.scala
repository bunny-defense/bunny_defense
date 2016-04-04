
package gui

import collection.mutable.ListBuffer
import util.Random

import java.awt.Graphics2D
import java.awt.AlphaComposite

import runtime.TowerDefense
import game_mechanics.path.Waypoint

object RainAnimation
{
    val fade_duration      = 1.0
    val max_darkness       = 0.3f
    val rng                = new Random
    val rain_color         = Colors.lightblue.darker()
    val speed              = 500
}

class RainAnimation(duration : Double) extends Animatable
{
    import RainAnimation._
    timer = duration

    var particles = new ListBuffer[Waypoint]

    def new_particle(): Unit = {
        val new_particle = new Waypoint(
            rng.nextDouble *
            (TowerDefense.map_panel.map.width + TowerDefense.map_panel.map.height)
                * MapPanel.cellsize,
            rng.nextDouble * -TowerDefense.map_panel.map.width * MapPanel.cellsize )
        new_particle.x -= new_particle.y
        particles += new_particle
    }

    for( i <- 0 until 100 )
    {
        new_particle()
    }

    override def draw(g : Graphics2D): Unit = {
        val alpha = Math.min(
            duration / 2 - Math.abs(timer - duration / 2),
            1.0 ).toFloat
        val darkness = alpha * max_darkness
        TowerDefense.map_panel.darkness = darkness
        g.setColor( rain_color )
        g.setComposite(
            AlphaComposite.getInstance( AlphaComposite.SRC_OVER, alpha ) )
        val loop = 2 * TowerDefense.map_panel.map.height * MapPanel.cellsize
        for( particle <- particles )
        {
            val pos = (particle.y + (duration - timer) * speed) % loop - particle.y
            g.drawLine(
                (particle.x - pos).toInt,
                (particle.y + pos).toInt,
                (particle.x - pos + 100).toInt,
                (particle.y + pos - 100).toInt )
        }
    }
}
