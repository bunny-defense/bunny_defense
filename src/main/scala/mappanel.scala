
package gui

import swing._

class MapPanel(rows: Int,cols: Int) extends GridPanel(rows,cols) {
  
  for( i <- 0 until rows * cols ) {
    contents += new MapButton {
      action = Action("") {
        println( i % cols, i / cols )
      }
    }
  }

}
