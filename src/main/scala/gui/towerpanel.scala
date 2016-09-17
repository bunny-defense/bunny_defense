
package gui

import swing._
import swing.event._

import java.awt.AlphaComposite
import java.awt.Graphics2D
import java.awt.MouseInfo

import runtime._
import game_mechanics._
import game_mechanics.tower._
import game_mechanics.path.CellPos

/* An info Panel that shows information on the selected tower */

object TowerInfoPanel {
  val default_size = new CellPos(200,100)
}

class TowerInfoPanel(parent: Option[TDComponent], gamestate: GuiGameState)
extends TDComponent(parent) {
  import TowerInfoPanel._
  val button_width = 200
  val xm = size.x
  val ym = size.y

  override def draw(g: Graphics2D): Unit = {
    super.draw(g)

    val windowpos = locationOnScreen
    val mousepos  = MouseInfo.getPointerInfo().getLocation()
    val mousex    = mousepos.x - windowpos.x
    val mousey    = mousepos.y - windowpos.y

    /* Info panel */
    g.setColor( Colors.black )
    g.drawRect( button_width, 0, button_width + xm-1, ym-1 )
    gamestate.selected_cell match {
      case None =>  {}
      case Some(tower) => {
        g.drawString(tower.towertype.name,
          button_width + xm / 2 - 34,
          ym / 4 + 5)
        g.drawString("Range :" + tower.range,
          button_width + xm / 3 - 34,
          2 * ym / 4 + 5)
        g.drawString("Projectile speed :" + tower.throw_speed,
          button_width + 2 * xm / 3 - 34,
          2 * ym / 4 + 5)
        g.drawString("Damage :" + tower.damage,
          button_width + xm / 3 - 34,
          3 * ym / 4 + 5)
        g.drawString("Sell price :" + tower.sell_cost,
          button_width + 2 * xm / 3 - 34,
          3 * ym / 4 + 5)
      }
    }
  }

  /* Sell button */
  val sell_button = new TextButton(Some(this), "Sell Tower") {
    pos = new CellPos( xm - button_width, 0)
    size =  new CellPos(button_width, ym/2)
    color = Colors.lightGrey
    text_color = Colors.black
    enabled = false

    override def action() : Unit = {
      val tower = gamestate.selected_cell.get
      gamestate.sell_tower(tower)
      gamestate.selected_cell = None
    }
  }

  /* Upgrade Button */
  val upgrade_button = new TextButton(Some(this), "Upgrade Tower") {
    pos = new CellPos( xm - button_width, ym/2)
    size = new CellPos( button_width, ym/2)
    color = Colors.red
    text_color = Colors.blue

    override def action() : Unit = {
      val tower = gamestate.selected_cell.get
      gamestate.upgrade_tower(tower)
    }
  }

  if (!gamestate.multiplayer) {
    /* Fast forward button */
    val ff_button = new TextButton(Some(this), "Fast Forward") {
      pos = new CellPos( 0, 0 )
      size = new CellPos ( button_width, ym )
      color = Colors.blue
      text_color = Colors.black

      override def action() {
        gamestate.acceleration = 4
      }
    }
  }
}

