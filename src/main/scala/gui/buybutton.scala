
package gui

import swing._

import runtime.Controller
import game_mechanics.{Tower,Player,MoneyChanged}

object BuyButton {
  val cellsize = 60
  val dimension = new Dimension (cellsize, cellsize)
}

class BuyButton(tower0: Option[Tower]) extends Button {
  this.preferredSize = BuyButton.dimension
  this.background    = Colors.white
  this.focusable     = false

  val tower          = tower0

  listenTo(Player)

  reactions += {
    case MoneyChanged =>
      if( tower != None )
        this.enabled = Player.gold >= tower.get.buy_cost
  }
}
