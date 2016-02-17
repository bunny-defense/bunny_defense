
package gui

import swing._

import runtime.controller
import game_mechanics.tower

class TowerPanel() extends Panel {
  background = Colors.lightGrey
  val tower = Controller.selected_cell
  override def paintComponent(g: Graphics2D): Unit = {
    super.paintComponent(g)
    val xm = size.width
    val ym = size.height
    g.drawString("Radius :"  + tower.range, (xm/3-34, ym/3 + 5)
    g.drawString("Speed :"   + tower.throw_speed, (2*xm/3-34, ym/3 + 5)
    g.drawString("Damages :" + tower.damages, (xm/3-34, 2*ym/3 + 5)
    g.drawString("Sell :"    + tower.sell_cost, (2*xm/3-34, 2*ym/3 + 5)
  }
}
