
package gui.animations

import java.awt.Graphics2D
import java.awt.AlphaComposite

import runtime.TowerDefense
import gui._

/* This animation creates a short flash that simulates thunder */

class ThunderflashAnimation(gamestate: ClientGameState)
extends Animatable(gamestate)
{
    val duration = 0.5
    timer = duration

    override def draw(g : Graphics2D): Unit = {
        g.setColor( Colors.white )
        val alpha = Math.pow( timer / duration, 4 ).toFloat
        g.setComposite(
            AlphaComposite.getInstance( AlphaComposite.SRC_OVER, alpha ) )
        g.fillRect( 0, 0,
            gamestate.map.width  * MapPanel.cellsize,
            gamestate.map.height * MapPanel.cellsize )
    }
}
