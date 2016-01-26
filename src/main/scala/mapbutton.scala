
package gui

import swing._

/* Reprensents a map cell on the screen as a clickable button */
object MapButton {
  val cellsize  = 40
  val dimension = new Dimension( cellsize, cellsize )
  val white     = new Color( 255, 255, 255 )
  val blue      = new Color( 0, 0, 0 )
}

class MapButton extends Button {
  this.preferredSize = MapButton.dimension
  this.background    = MapButton.white
}
