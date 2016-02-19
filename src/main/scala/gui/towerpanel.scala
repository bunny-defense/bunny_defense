
package gui

import swing._

import runtime.Controller
import game_mechanics._

class TowerPanel() extends Panel {
  background = Colors.lightGrey
  preferredSize = new Dimension( 10, 100 )
  override def paintComponent(g: Graphics2D): Unit = {
    super.paintComponent(g)
    val xm = size.width
    val ym = size.height
    Controller.selected_cell match {
      case None =>  {}
      case Some(tower) => {
        g.drawString("Radius :"  + tower.range, xm/3-34, ym/3 + 5)
        g.drawString("Speed :"   + tower.throw_speed, 2*xm/3-34, ym/3 + 5)
        g.drawString("Damages :" + tower.damage, xm/3-34, 2*ym/3 + 5)
        g.drawString("Sell :"    + tower.sell_cost, 2*xm/3-34, 2*ym/3 + 5)
      }
    }
  }
}
