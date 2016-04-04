
package gui

import java.awt.Graphics2D

import game_mechanics.path.Waypoint

/* An animation that creates a muzzle flash when turrets shoot */
class MuzzleflashAnimation(origin : Waypoint) extends Animatable
{
    val duration = 0.1
    timer        = duration
    val cellsize = MapPanel.cellsize
    val size     = cellsize / 2
    val pos      = origin * cellsize

    override def draw(g: Graphics2D): Unit = {
        g.setColor( Colors.white )
        g.fillOval(
            pos.x.toInt + cellsize / 2 - size / 2,
            pos.y.toInt + cellsize / 2 - size / 2,
            size, size )
    }
}
