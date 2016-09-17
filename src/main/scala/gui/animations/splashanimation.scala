
package gui.animations

import java.awt.AlphaComposite
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import game_mechanics.path.Waypoint
import runtime.TowerDefense
import runtime.GuiGameState
import gui._
import utils.Continuable

class SplashAnimation(gamestate: GuiGameState)
extends Animatable(gamestate)
{
    val height = 100
    val duration = 3.0
    timer = duration

    val origin = new Waypoint(
        0,
        map_panel.size.y / 2 )
    val target = origin + new Waypoint(
        map_panel.size.x,
        0 )

    override def draw(g: Graphics2D): Unit = {
        val string = "Boss incoming"
        val strwidth = g.getFontMetrics().stringWidth( string )
        val interp = Math.pow( timer * 2 / duration - 1, 3 ) / 2 + 0.5
        val pos = origin * interp + target * ( 1 - interp )
        var alpha = (duration / 2 - Math.abs( timer - duration / 2 )).toFloat
        if( alpha > 1.0f )
            alpha = 1.0f
        g.setComposite( AlphaComposite.getInstance(
            AlphaComposite.SRC_OVER, alpha ) )
        g.setColor( Colors.darkred )
        g.fillRect(
            0,
            map_panel.size.y / 2 - height / 2,
            map_panel.size.x,
            height)
        g.setColor( Colors.yellow )
        g.drawString( string,
            pos.x.toFloat - strwidth.toFloat / 2,
            pos.y toFloat )
    }
}
