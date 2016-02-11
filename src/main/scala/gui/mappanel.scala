
package gui

import swing._
import swing.event._

import java.awt.image.BufferedImage
import java.awt.MouseInfo
import java.io.File
import javax.imageio.ImageIO

import runtime.Controller
import game_mechanics.GameMap

object MapPanel
{
  val cellsize     = 32
  val bunny_sprite = ImageIO.read(new File(getClass().getResource("/mobs/bunny1.png").getPath()))
}

/* Represents the map on the screen */
class MapPanel(map0: GameMap) extends Panel {
  import MapPanel._
  val map  = map0
  val rows = map.height
  val cols = map.width

  preferredSize = new Dimension(
    cellsize * cols,
    cellsize * rows )

  listenTo(mouse.clicks)

  reactions += {
    case e: MouseClicked =>
      Controller.on_cell_clicked(
        e.point.x / cellsize,
        e.point.y / cellsize )
  }

  /* Drawing on the map */
  override def paintComponent(g: Graphics2D): Unit = {
    super.paintComponent(g)
    /* Drawing the map */
    for( x <- 0 until cols ) {
      for( y <- 0 until rows ) {
        g.drawImage( map.graphic_map(x)(y),
          x * cellsize,
          y * cellsize, null )
      }
    }
    /* Drawing the bunnies */
    for( bunny <- Controller.bunnies )
    {
      val x = bunny.pos.x * cellsize
      val y = bunny.pos.y * cellsize
      g.drawImage( bunny_sprite, x.toInt, y.toInt, null )
    }
    for( tower <- Controller.towers )
    {
      val x = tower.pos.x * cellsize
      val y = tower.pos.y * cellsize
      g.drawImage( tower.graphic, x.toInt, y.toInt, null )
    }
    for( projectile <- Controller.projectiles )
    {
      val x = projectile.pos.x * cellsize
      val y = projectile.pos.y * cellsize
      g.drawImage( projectile.graphic, x.toInt, y.toInt, null )
    }
    Controller.selected_tower match {
      case None => {}
      case Some(tower) => {
        val mousepos  = MouseInfo.getPointerInfo().getLocation()
        val windowpos = locationOnScreen
        val snapx     = (mousepos.x - windowpos.x) / cellsize * cellsize
        val snapy     = (mousepos.y - windowpos.y) / cellsize * cellsize
        g.drawRect( snapx, snapy, cellsize, cellsize )
        g.drawImage( tower.graphic, snapx, snapy, null )
        val range   = tower.range * cellsize
        val circlex = snapx + cellsize / 2 - range
        val circley = snapy + cellsize / 2 - range
        g.drawOval( circlex, circley, 2 * range, 2 * range )
      }
    }
    /*g.drawImage( bunny_sprite, cellsize, cellsize, null )*/
  }

}
