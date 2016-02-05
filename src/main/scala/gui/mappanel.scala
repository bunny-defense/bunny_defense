
package gui

import swing._
import swing.event._


import runtime.TowerDefense
import game_mechanics.GameMap

/* Represents the map on the screen */
class MapPanel(map: GameMap) extends Panel {

  val rows = map.height
  val cols = map.width

  preferredSize = new Dimension(
    MapButton.cellsize * cols,
    MapButton.cellsize * rows )

  listenTo(mouse.clicks)

  reactions += {
    case e: MouseClicked =>
      TowerDefense.on_cell_clicked(
        e.point.x / MapButton.cellsize,
        e.point.y / MapButton.cellsize )
  }

  /* Drawing on the map */
  override def paintComponent(g: Graphics2D): Unit = {
    super.paintComponent(g)
    g.setColor( Colors.black )
    for( i <- 1 until rows ) {
      g.drawLine(
        0,
        i * MapButton.cellsize,
        MapButton.cellsize * cols,
        i * MapButton.cellsize )
    }
    for( i <- 1 until cols ) {
      g.drawLine(
        i * MapButton.cellsize,
        0,
        i * MapButton.cellsize,
        MapButton.cellsize * rows )
    }
    for( x <- 0 until cols ) {
      for( y <- 0 until rows ) {
        g.drawImage( map.graphic_map(x)(y),
          x * MapButton.cellsize,
          y * MapButton.cellsize, null )
      }
    }
  }

}
