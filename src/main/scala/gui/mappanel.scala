
package gui

import swing._
import swing.event._

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import runtime.{TowerDefense,Controller}
import game_mechanics.GameMap

object MapPanel
{
  val cellsize     = 32
  val bunny_sprite = ImageIO.read(new File(getClass().getResource("/mobs/bunny1.png").getPath()))
}

/* Represents the map on the screen */
class MapPanel(map: GameMap) extends Panel {
  import MapPanel._
  val rows = map.height
  val cols = map.width

  preferredSize = new Dimension(
    cellsize * cols,
    cellsize * rows )

  listenTo(mouse.clicks)

  reactions += {
    case e: MouseClicked =>
      TowerDefense.on_cell_clicked(
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
    /*g.drawImage( bunny_sprite, cellsize, cellsize, null )*/
  }

}
