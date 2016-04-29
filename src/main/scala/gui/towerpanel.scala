
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
                y >= 0 && y < size.height / 2 &&
                !TowerDefense.gamestate.selected_cell.isEmpty )
            {
                val tower = TowerDefense.gamestate.selected_cell.get
                TowerDefense.gamestate -= tower
                Player.add_gold(tower.sell_cost)
                TowerDefense.gamestate.selected_cell = None
            }
            if( x >= 0 && x < button_width &&
                y >= 0 && y < size.height )
            {
                TowerDefense.gamestate.on_fastforward_button()
            }
            if (x >= size.width - button_width &&
                x < size.width &&
                y >= size.height / 2 && y < size.height &&
                !TowerDefense.gamestate.selected_cell.isEmpty)
            {
                TowerDefense.gamestate.upgrade_tower()
            }
        }
        case MousePressed(_,_,_,_,_)  =>
            clicked = true
        case MouseReleased(_,_,_,_,_) =>
            clicked = false
    }

        /* Upgrade Button tooltip */

        def draw_tooltip(g : Graphics2D)
        {
            val upgrade_button_rect = new Rectangle(
                size.width - button_width, size.height/2, button_width, size.height/2 )
            /* A copy of the upgrade button; we'll just use it to get its position */
            val windowpos = locationOnScreen
            val mousepos  = MouseInfo.getPointerInfo().getLocation()
            val mousex    = mousepos.x - windowpos.x
            val mousey    = mousepos.y - windowpos.y
            if( upgrade_button_rect.contains( mousex, mousey ) )
            {
                var upgrade_name = ""
                var upgrade_desc = ""
                var upgrade_cost = ""
                if (!TowerDefense.gamestate.selected_cell.isEmpty &&
                    !TowerDefense.gamestate.selected_cell.get.upgrades.isEmpty) {
                    upgrade_name = TowerDefense.gamestate.selected_cell.get.upgrades.get.name
                    upgrade_desc = TowerDefense.gamestate.selected_cell.get.upgrades.get.description
                    upgrade_cost = TowerDefense.gamestate.selected_cell.get.upgrades.get.cost.toString + " gold"
                }
                val name_size = g.getFontMetrics().stringWidth( upgrade_name )
                val desc_size = g.getFontMetrics().stringWidth( upgrade_desc )
                val cost_size = g.getFontMetrics().stringWidth( upgrade_cost )
                val width = Math.max( Math.max( name_size, desc_size ), cost_size ) + 4
                val height = 49
                val rect = new Rectangle(
                    mousepos.x.toInt - width, mousepos.y.toInt - height,
                    width, height )
                if (!TowerDefense.gamestate.selected_cell.isEmpty &&
                    !TowerDefense.gamestate.selected_cell.get.upgrades.isEmpty) {
                    g.setColor( Colors.lightGrey )
                    g.fill( rect )
                    g.setColor( Colors.black )
                    g.draw( rect )
                        g.drawString( upgrade_name,
                        mousepos.x.toInt - width + 2, mousepos.y.toInt - height + 15 )
                    g.drawString( upgrade_desc,
                        mousepos.x.toInt - width + 2, mousepos.y.toInt - height + 30 )
                    g.drawString( upgrade_cost,
                        mousepos.x.toInt - width + 2, mousepos.y.toInt - height + 45 )
                }
            }
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
        TowerDefense.gamestate.selected_cell match {
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
            size.width - button_width, 0, button_width, ym/2)
        g.setColor( Colors.white )
        g.fill( sell_button_rect )
        g.setColor( Colors.black )
        g.draw( sell_button_rect )
        val sell_text = "Sell tower"
        val sell_text_width = g.getFontMetrics().stringWidth( sell_text )
        if( TowerDefense.gamestate.selected_cell == None )
            g.setColor( Colors.lightGrey )
        g.drawString( sell_text,
            button_width + xm + button_width / 2 - sell_text_width / 2,
            ym / 4 )
        if( sell_button_rect.contains( mousex, mousey ) &&
            TowerDefense.gamestate.selected_cell != None )
        {
            val alpha = if( clicked ) 0.7f else 0.5f
            g.setComposite(
                AlphaComposite.getInstance( AlphaComposite.SRC_OVER, alpha ) )
            g.setColor( Colors.lightblue )
            g.fill( sell_button_rect )
        }
        g.setComposite(
            AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 1f ) )

        /* Upgrade Button */
       val upgrade_button_rect = new Rectangle(
           size.width - button_width, ym/2, button_width, ym / 2 )
       g.setColor( Colors.white )
       g.fill( upgrade_button_rect )
       g.setColor (Colors.black )
       g.draw( upgrade_button_rect )
       var upgrade_text = ""
       if (!TowerDefense.gamestate.selected_cell.isEmpty &&
           !TowerDefense.gamestate.selected_cell.get.upgrades.isEmpty) {
           upgrade_text = TowerDefense.gamestate.selected_cell.get.upgrades.get.name
       }
       val upgrade_text_width = g.getFontMetrics().stringWidth( upgrade_text )
       g.drawString(
           upgrade_text,
           button_width + xm + button_width / 2 - upgrade_text_width / 2,
            3 * ym / 4 )
       if ( upgrade_button_rect.contains( mousex, mousey ) )
       {
           var alpha = if( clicked ) 0.7f else 0.5f
           g.setComposite(
               AlphaComposite.getInstance( AlphaComposite.SRC_OVER, alpha ) )
           g.setColor( Colors.lightblue )
           g.fill( upgrade_button_rect )
           g.setComposite(
               AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 1f )
           )
       }

        /* Fast forward button */
        val ff_button_rect = new Rectangle( 0, 0, button_width, ym )
        g.setColor( Colors.blue )
        g.fill( ff_button_rect )
        g.setColor( Colors.black )
        g.draw( ff_button_rect )
        val ff_text = "Fast forward: " +
            (if( TowerDefense.gamestate.is_accelerated ) "ON" else "OFF")
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

