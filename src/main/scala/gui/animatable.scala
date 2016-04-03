
package gui

import java.awt.AlphaComposite
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import game_mechanics.bunny.BunnyType
import game_mechanics.path.Waypoint
import runtime.{TowerDefense,Controller}
import utils.Continuable

/* Animation superclass */

abstract class Animatable extends Continuable
{
    var timer  = 1.0

    def on_timer_ran_out(): Unit = {
        Controller -= this
        continue()
    }

    def update(dt: Double): Unit = {
        timer -= dt
        if( timer <= 0 )
            on_timer_ran_out()
    }

    def draw(g: Graphics2D): Unit
}

/* Creates nice animations when bunnies die */

class GoldAnimation(amount: Int,origin: Waypoint) extends Animatable
{
    timer = 2.0
    var pos    = origin
    val target = origin + new Waypoint(0,-1)

    override def draw(g: Graphics2D): Unit = {
        pos = origin * timer + target * (1 - timer)
        val string = "+" + amount.toString + " Gold"
        val alpha  = if(timer < 1.0) { timer.toFloat } else { 1.0f }
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

/* Creates a moving "wave #" to indicate the wave number */

object WaveAnimation
{
    val background = ImageIO.read(
        new File(
            getClass().getResource("/UI/Button_Dark.png").getPath()))
    val image_origin_x : Int = background.getWidth() / 2
    val image_origin_y : Int = background.getHeight() / 2
}

class WaveAnimation(wave_number: Int) extends Animatable
{
    import WaveAnimation._

    val duration = 2.0
    timer = duration

    val origin = new Waypoint( -image_origin_x.toDouble, TowerDefense.map_panel.size.height / 2 )
    val target = origin + new Waypoint( TowerDefense.map_panel.size.width + image_origin_x.toDouble, 0 )

    override def draw(g: Graphics2D): Unit = {
        val interp = Math.pow( timer * 2 / duration - 1, 3 ) / 2 + 0.5
        val pos = origin * interp + target * ( 1 - interp )
        val string = "Wave " + wave_number.toString
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

class SplashAnimation(boss : BunnyType) extends Animatable
{
    val height = 100
    val duration = 3.0
    timer = duration

    val origin = new Waypoint( 0, TowerDefense.map_panel.size.height / 2 )
    val target = origin + new Waypoint( TowerDefense.map_panel.size.width, 0 )

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
            TowerDefense.map_panel.size.height / 2 - height / 2,
            TowerDefense.map_panel.size.width,
            height)
        g.setColor( Colors.yellow )
        g.drawString( string,
            pos.x.toFloat - strwidth.toFloat / 2,
            pos.y toFloat )
    }
}
