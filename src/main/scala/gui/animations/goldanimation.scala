
package gui.animations

import java.awt.AlphaComposite
import java.awt.Graphics2D

import game_mechanics.path.Waypoint
import gui._
import runtime.GuiGameState
import utils.Continuable



class GoldAnimation(amount: Int,origin: Waypoint, gamestate: GuiGameState)
extends Animatable(gamestate)
{
    /* Creates nice animations when bunnies die */
    timer = 2.0
    var pos    = origin
    val target = origin + new Waypoint(0,-1)

    override def draw(g: Graphics2D): Unit = {
        pos = origin * timer + target * (1 - timer)
        val string = "+" + amount.toString + " Gold"
        val alpha  = if(timer < 1.0) { Math.max(0,timer).toFloat } else { 1.0f }
        g.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, alpha ) )
        g.setColor( Colors.black )
        g.drawString( string,
            pos.x.toFloat * MapPanel.cellsize + 1,
            pos.y.toFloat * MapPanel.cellsize + 1 )
        g.setColor( Colors.yellow )
        g.drawString( string,
            pos.x.toFloat * MapPanel.cellsize,
            pos.y.toFloat * MapPanel.cellsize )
    }
}
