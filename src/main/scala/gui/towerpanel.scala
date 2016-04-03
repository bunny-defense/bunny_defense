
package gui

import swing._

import runtime.{Controller, SelectedCell, NoSelectedCell, FastForwOn, FastForwOff}
import game_mechanics._
import game_mechanics.tower._

/* An info Panel that shows information of the selected tower */

class TowerInfoPanel() extends Panel {
    background = Colors.lightGrey
    override def paintComponent(g: Graphics2D): Unit = {
        super.paintComponent(g)
        val xm = size.width
        val ym = size.height
        g.setColor( Colors.black )
        g.drawRect( 0, 0, xm-1, ym-1 )
        Controller.selected_cell match {
            case None =>  {}
            case Some(tower) => {
                g.drawString(tower.towertype.name, xm/2-34, ym/4+5)
                g.drawString("Range :" + tower.range, xm/3-34, 2*ym/4 + 5)
                g.drawString("Projectile speed :" + tower.throw_speed, 2*xm/3-34, 2*ym/4 + 5)
                g.drawString("Damage :" + tower.damage, xm/3-34, 3*ym/4 + 5)
                g.drawString("Sell price :" + tower.sell_cost, 2*xm/3-34, 3*ym/4 + 5)
            }
        }
    }
}

class TowerPanel() extends BorderPanel {
    val sell_button = new Button {
        action = Action("") {
            Controller -= Controller.selected_cell.get
            Player.add_gold(Controller.selected_cell.get.sell_cost)
            Controller.selected_cell = None
        }
        preferredSize = new Dimension( 200, 100 )
        background = Colors.lightgreen
        enabled = false
        listenTo(Controller)
        reactions += {
            case SelectedCell   => enabled = true
            case NoSelectedCell => enabled = false
        }
        text = "Sell Tower"
    }

    val upgrade_button = new Button {
        action = Action("") {
            Controller.selected_cell match
            {
                case None => ()
                case Some(tower) => {
                    tower.upgrades match
                    {
                        case None => { }
                        case Some(upgrade) => {
                            if(Player.gold >= upgrade.cost) {
                                upgrade.effect(tower)
                                Player.remove_gold(upgrade.cost)
                            }
                            else {
                                println("Not enough gold to buy tower upgrade !")
                            }
                        }
                    }
                }
            }
        }
        preferredSize = new Dimension ( 200, 100 )
        background = Colors.lightblue
        enabled = false
        listenTo(Controller)
        reactions += {
            case SelectedCell   => {
                enabled = true
                Controller.selected_cell match
                {
                    case None => ()
                    case Some(tower) => {
                        /* def paintComponent(g : Graphics2D) : Unit = {
                            super.paintComponent(g)
                            val xm = size.width
                            val ym = size.height
                            g.setColor( Colors.black )
                            g.drawRect( 0, 0, xm-1, ym-1 )
                            g.drawString( tower.upgrades.name, xm/2-34, ym/4+5 )
                            g.drawString( tower.upgrades.cost + " gold", xm/2-34, ym/4+5 )
                            g.drawString( tower.upgrades.description, xm/2-34, ym/4+5 )
                        }
                        text = Controller.selected_cell.get.upgrades.name */
                    }
                }
            }
            case NoSelectedCell => {
                enabled = false
                Controller.selected_cell match
                {
                    case None => ()
                    case Some(tower) => tower.upgrades match
                    {
                        case None => { }
                        case Some(upgrade) => text = upgrade.name
                    }
                }
            }
        }
    }

    val fastforward_button = new Button {
        action = Action("") { Controller.on_fastforward_button() }
        focusable = false
        listenTo(Controller)
        text = "Fast forward : OFF"
        reactions += {
            case FastForwOn =>
                text = "Fast forward : ON"
            case FastForwOff =>
                text = "Fast forward : OFF"
        }
        background = Colors.midblue
        preferredSize = new Dimension( 250, 100 )
    }
    val thepanel = new TowerInfoPanel
    val borderpanel = new BorderPanel {
        layout(upgrade_button) = BorderPanel.Position.Center
        layout(sell_button) = BorderPanel.Position.East
        }
    add( thepanel, BorderPanel.Position.Center )
    add( borderpanel, BorderPanel.Position.East )
    add( fastforward_button, BorderPanel.Position.West )
}
