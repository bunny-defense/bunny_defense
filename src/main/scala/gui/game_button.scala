
package gui

import swing._

object BuyButton {
  val cellsize = 60
  val dimension = new Dimension (cellsize, cellsize)
}

class BuyButton extends Button {
  this.preferredSize = BuyButton.dimension
  this.background    = Colors.white
}
