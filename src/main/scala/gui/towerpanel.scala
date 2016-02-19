
package gui

import swing._

import runtime.{Controller, SelectedCell, NoSelectedCell}
import game_mechanics._



class ThePanel() extends Panel {
  background = Colors.lightGrey
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

class TowerPanel() extends BoxPanel(Orientation.Horizontal) {
  val sell_button = new Button {
    action = Action("") {
      Controller.towers -= Controller.selected_cell.get
      Player.add_gold(Controller.selected_cell.get.sell_cost)
      Controller.selected_cell = None
    }
    preferredSize = new Dimension( 200, 100 )
    enabled = false
    listenTo(Controller)
    reactions += {
      case SelectedCell   => enabled = true
      case NoSelectedCell => enabled = false
    }
    text = "Sell Tower"
  }
  val thepanel = new ThePanel
  contents += thepanel
  contents += sell_button
}
