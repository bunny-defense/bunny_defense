
package gui.animations

import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import game_mechanics.path.Waypoint
import runtime.TowerDefense
import runtime.GuiGameState
import gui._
import utils.Continuable

object SpreadAnimation
{
    val background = ImageIO.read(
        new File(
            getClass().getResource("/projectiles/potato-image.png").getPath()))
}

class SpreadAnimation(
    pos: Waypoint, radius_init: Int, dir: Waypoint, gamestate: GuiGameState)
extends Animatable(gamestate)
{
    import SpreadAnimation._
    val origin = pos.toInt * MapPanel.cellsize
    val radius = radius_init * MapPanel.cellsize
    val duration = 0.5
    timer = duration
    val target = (pos +  dir * radius_init).toInt * MapPanel.cellsize


    override def draw(g: Graphics2D): Unit = {
        val interp = Math.pow(timer / duration, 1.0/5.0)
        val pos = origin * interp +  target * (1 - interp )
        g.drawImage(background,
            pos.x.toInt,
            pos.y.toInt,
            null )
    }
}
