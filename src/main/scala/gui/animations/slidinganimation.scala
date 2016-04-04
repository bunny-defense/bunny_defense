
package gui.animations

import java.awt.AlphaComposite
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import game_mechanics.bunny.BunnyType
import game_mechanics.path.Waypoint
import runtime.{TowerDefense,Controller}
import gui._
import utils.Continuable

/* Creates a moving "wave #" to indicate the wave number */

object SlidingAnimation
{
    val background = ImageIO.read(
        new File(
            getClass().getResource("/UI/Button_Dark.png").getPath()))
    val image_origin_x : Int = background.getWidth() / 2
    val image_origin_y : Int = background.getHeight() / 2
}

class SlidingAnimation(text_callback : () => String) extends Animatable
{
    import SlidingAnimation._

    val duration = 5.0
    timer = duration

    val origin = new Waypoint( -image_origin_x.toDouble, TowerDefense.map_panel.size.height / 2 )
    val target = origin + new Waypoint( TowerDefense.map_panel.size.width + image_origin_x.toDouble, 0 )

    override def draw(g: Graphics2D): Unit = {
        val interp = Math.pow( timer * 2 / duration - 1, 5 ) / 2 + 0.5
        val pos = origin * interp + target * ( 1 - interp )
        val string = text_callback()
        val strwidth = g.getFontMetrics().stringWidth( string )
        g.drawImage( background,
            pos.x.toInt - image_origin_x,
            pos.y.toInt - image_origin_y,
            null )
        g.setColor( Colors.white )
        g.drawString( string,
            pos.x.toFloat - strwidth.toFloat / 2,
            pos.y.toFloat )
    }
}

class WaveAnimation(wave_number : Int)
extends SlidingAnimation( () => "Wave " + wave_number.toString )
