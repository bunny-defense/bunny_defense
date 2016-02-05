
package gui

import swing._
import swing.event._
import runtime.TowerDefense

/* Represents the map on the screen */
class MapPanel(rows: Int,cols: Int) extends Panel {

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
  }

}
