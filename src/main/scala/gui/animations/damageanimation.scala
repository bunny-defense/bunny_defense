
package gui.animations

import java.awt.AlphaComposite
import java.awt.Graphics2D

import game_mechanics.path.Waypoint
import gui._
import runtime.{TowerDefense,Controller}
import utils.Continuable

class DamageAnimation(amount: Double, origin: Waypoint) extends Animatable
{
    var pos    = origin
    val target = origin + new Waypoint(0,-1)

    override def draw(g: Graphics2D): Unit = {
    /*    return // TODO Make this animation useful */
        pos = origin * timer + target * (1 - timer)
        g.setColor( Colors.red )
        g.drawString( amount.toString + " dmg",
            pos.x.toFloat * MapPanel.cellsize,
            pos.y.toFloat * MapPanel.cellsize )
    }
}
