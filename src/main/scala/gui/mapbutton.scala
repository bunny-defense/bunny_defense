
package gui

import swing._

/* Reprensents a map cell on the screen as a clickable button */
object MapButton {
  val cellsize  = 32
  val dimension = new Dimension( cellsize, cellsize )
}

class MapButton extends Button {
  this.preferredSize = MapButton.dimension
  this.background    = Colors.white
}
