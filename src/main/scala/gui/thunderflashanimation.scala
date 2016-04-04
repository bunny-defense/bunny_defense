
package gui

import java.awt.Graphics2D
import java.awt.AlphaComposite

import runtime.TowerDefense

/* This animation creates a short flash that simulates thunder */

class ThunderflashAnimation extends Animatable
{
    val duration = 0.5
    timer = duration

    override def draw(g : Graphics2D): Unit = {
        g.setColor( Colors.white )
        val alpha = Math.pow( timer / duration, 4 ).toFloat
        g.setComposite(
            AlphaComposite.getInstance( AlphaComposite.SRC_OVER, alpha ) )
        g.fillRect( 0, 0,
            TowerDefense.map_panel.map.width  * MapPanel.cellsize,
            TowerDefense.map_panel.map.height * MapPanel.cellsize )
    }
}
