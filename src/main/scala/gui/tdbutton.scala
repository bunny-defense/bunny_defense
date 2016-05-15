
package gui

import java.awt.Graphics2D
import java.awt.MouseInfo

import swing.Panel
import swing.event._

import runtime.StateManager

/* Abstract class representing the buttons in the gui */

abstract class TDButton(parent: Option[TDComponent])
extends TDComponent(parent)
{
    var enabled = true
    override def on_event(event: Event) : Unit = {
        super.on_event(event)
        event match {
            case e : MouseReleased => {
                if( enabled && is_hovered() )
                    action()
            }
            case _ => {}
        }
    }
    /* This function is called when the button is clicked */
    def action() : Unit = {}

    protected def is_hovered() : Boolean = {
        val screenpos = locationOnScreen
        val mousepos  = MouseInfo.getPointerInfo().getLocation()
        val mousex = mousepos.x - screenpos.x
        val mousey = mousepos.y - screenpos.y
        return mousex >= 0 && mousey >= 0 &&
               mousex < size.x && mousey < size.y
    }
    /* This function says how to draw the button */
    def draw_contents(g: Graphics2D) : Unit
    override def draw(g: Graphics2D) : Unit = {
        draw_contents(g)
        if( is_hovered() )
        {
            g.setColor( Colors.transparent_cyan )
            g.fillRect( 0, 0, size.x, size.y )
        }
    }
}

/* This button has a square background and a text on it */
class TextButton(parent: Option[TDComponent], text : String)
extends TDButton(parent)
{
    var color = Colors.red
    var text_color = Colors.white
    override def draw_contents(g: Graphics2D) : Unit = {
        g.setColor( color )
        g.fillRect( 0, 0, size.x, size.y )
        g.setColor( text_color )
        val stringwidth = g.getFontMetrics().stringWidth( text )
        g.drawString( text,
            size.x / 2 - stringwidth / 2, size.y / 2 )
    }
}
