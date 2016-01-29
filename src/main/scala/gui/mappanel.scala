
package gui

import swing._
import runtime.TowerDefense

/* Represents the map on the screen */
class MapPanel(rows: Int,cols: Int) extends GridPanel(rows,cols) {

  for( i <- 0 until rows * cols ) {
    contents += new MapButton {
      action = Action("") {
        TowerDefense.on_cell_clicked( i % cols, i / cols )
      }
    }
  }

}
