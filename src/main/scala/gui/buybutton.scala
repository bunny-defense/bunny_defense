
package gui

import swing._

import javax.swing.ImageIcon

import runtime._
import game_mechanics.{Tower,Player,MoneyChanged}

object BuyButton {
  val cellsize = 40
  val dimension = new Dimension (cellsize, cellsize)
}

class BuyButton(tower0: Option[Tower]) extends Button {
  import BuyButton._
  this.background    = Colors.white
  this.focusable     = false
  this.action        = Action("") { Controller.on_build_button(this) }
  if( tower0 != None )
    this.icon        = new ImageIcon( tower0.get.graphic )
  this.preferredSize = dimension

  val tower          = tower0

  listenTo(Player,SpawnScheduler)

  reactions += {
    case MoneyChanged => {
      if( tower != None )
        this.enabled = Player.gold >= tower.get.buy_cost
      if( this.enabled )
        this.background = Colors.white
      else
        this.background = Colors.lightred
    }
    case WaveStarted =>
      enabled = false
    case WaveEnded =>
      enabled = true
    }
}
