
package gui.animations

import collection.mutable.ListBuffer
import util.Random

import java.awt.Graphics2D
import java.awt.AlphaComposite

import runtime.TowerDefense
import runtime.GuiGameState
import gui._
import game_mechanics.path.{Waypoint,CellPos}

/* This is the unfinished raygun shooting animation (big laser) */

object RaygunShootAnimation
{
    val rng            = new Random
    val duration       = 10.0
}

class RaygunShootAnimation(
    tower_pos: CellPos, direction : Waypoint, gamestate: GuiGameState)
extends Animatable(gamestate)
{
    import RaygunShootAnimation._
    val origin         = tower_pos.toDouble
    timer = duration
    val size           = MapPanel.cellsize.toDouble
    val laser_length   = map_panel.size.y

    override def draw(g: Graphics2D): Unit = {
        val prev_transform = g.getTransform()
        g.rotate( Math.atan2( direction.y, direction.x ), tower_pos.x, tower_pos.y )
        val alpha = Math.min( timer, 1.0 ).toFloat
        g.setComposite(
            AlphaComposite.getInstance( AlphaComposite.SRC_OVER, alpha ) )
        g.setColor( Colors.white )
        g.fillRect(
            tower_pos.x * MapPanel.cellsize + MapPanel.cellsize / 2,
            tower_pos.y * MapPanel.cellsize - MapPanel.cellsize / 2,
            laser_length,
            MapPanel.cellsize * 2 )
        g.setColor( Colors.lightblue )
        g.fillRect(
            tower_pos.x * MapPanel.cellsize + MapPanel.cellsize / 2,
            tower_pos.y * MapPanel.cellsize,
            laser_length,
            MapPanel.cellsize )
        map_panel.darkness = (alpha * RaygunAnimation.max_darkness).toFloat
        g.setTransform( prev_transform )
    }
}
