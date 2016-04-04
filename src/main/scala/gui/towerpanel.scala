
package gui

import swing._
import swing.event._

import java.awt.AlphaComposite
import java.awt.Graphics2D
import java.awt.MouseInfo

import runtime._
import game_mechanics._
import game_mechanics.tower._

/* An info Panel that shows information on the selected tower */

class TowerInfoPanel() extends Panel {
    background = Colors.lightGrey
    preferredSize = new Dimension( 200, 100 )
    val button_width = 200
    var clicked = false

    listenTo(mouse.clicks)

    reactions += {
        case e : MouseClicked =>
        {
            val x = e.point.x
            val y = e.point.y
            if( x >= size.width - button_width &&
                x <  size.width &&
                y >= 0 && y < size.height &&
                Controller.selected_cell != None )
            {
                val tower = Controller.selected_cell.get
                Controller -= tower
                Player.add_gold(tower.sell_cost)
                Controller.selected_cell = None
            }
            if( x >= 0 && x < button_width &&
                y >= 0 && y < size.height )
            {
                Controller.on_fastforward_button()
            }
        }
        case MousePressed(_,_,_,_,_)  =>
            clicked = true
        case MouseReleased(_,_,_,_,_) =>
            clicked = false
    }

    override def paintComponent(g: Graphics2D): Unit = {
        super.paintComponent(g)

        val xm = size.width - 2 * button_width
        val ym = size.height
        val windowpos = locationOnScreen
        val mousepos  = MouseInfo.getPointerInfo().getLocation()
        val mousex    = mousepos.x - windowpos.x
        val mousey    = mousepos.y - windowpos.y

        /* Info panel */
        g.setColor( Colors.black )
        g.drawRect( button_width, 0, button_width + xm-1, ym-1 )
        Controller.selected_cell match {
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

        /* Sell button */
        val sell_button_rect = new Rectangle(
            size.width - button_width, 0, button_width, ym )
        g.setColor( Colors.white )
        g.fill( sell_button_rect )
        g.setColor( Colors.black )
        g.draw( sell_button_rect )
        val sell_text = "Sell tower"
        val sell_text_width = g.getFontMetrics().stringWidth( sell_text )
        if( Controller.selected_cell == None )
            g.setColor( Colors.lightGrey )
        g.drawString( sell_text,
            button_width + xm + button_width / 2 - sell_text_width / 2,
            ym / 2 )
        if( sell_button_rect.contains( mousex, mousey ) &&
            Controller.selected_cell != None )
        {
            val alpha = if( clicked ) 0.7f else 0.5f
            g.setComposite(
                AlphaComposite.getInstance( AlphaComposite.SRC_OVER, alpha ) )
            g.setColor( Colors.lightblue )
            g.fill( sell_button_rect )
        }
        g.setComposite(
            AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 1f ) )

        /* Fast forward button */
        val ff_button_rect = new Rectangle( 0, 0, button_width, ym )
        g.setColor( Colors.blue )
        g.fill( ff_button_rect )
        g.setColor( Colors.black )
        g.draw( ff_button_rect )
        val ff_text = "Fast forward: " +
            (if( Controller.is_accelerated ) "ON" else "OFF")
        val ff_text_width = g.getFontMetrics().stringWidth( ff_text )
        g.drawString( ff_text,
            button_width / 2 - ff_text_width / 2,
            ym / 2 )
        if( ff_button_rect.contains( mousex, mousey ) )
        {
            val alpha = if( clicked ) 0.7f else 0.5f
            g.setComposite(
                AlphaComposite.getInstance( AlphaComposite.SRC_OVER, alpha ) )
            g.setColor( Colors.lightblue )
            g.fill( ff_button_rect )
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
