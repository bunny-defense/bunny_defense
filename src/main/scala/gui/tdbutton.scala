
package gui

import java.awt.Graphics2D
import java.awt.MouseInfo

import swing.Panel
import swing.Rectangle
import swing.Reactor
import swing.event._

/* Abstract class representing the buttons in the gui */

abstract class TDButton( render_surface : Panel,
    x : Int, y : Int, width : Int, height : Int )
extends TDComponent
{
    override def on_click(posx: Int, posy: Int) : Unit = {
        if( is_hovered() )
            action()
    }
    /* This function is called when the button is clicked */
    def action() : Unit = {}

    protected val surface = new Rectangle( x, y, width, height )
    private def is_hovered() : Boolean = {
        val screenpos = render_surface.locationOnScreen
        val mousepos  = MouseInfo.getPointerInfo().getLocation()
        val mousex = mousepos.x - screenpos.x - x
        val mousey = mousepos.y - screenpos.y - y
        return mousex >= 0 && mousey >= 0 &&
               mousex <= width && mousey <= height
    }
    /* This function says how to draw the button */
    def draw_contents(g: Graphics2D) : Unit
    override def draw(g: Graphics2D) : Unit = {
        draw_contents(g)
        if( is_hovered() )
        {
            g.setColor( Colors.transparent_cyan )
            g.fill( surface )
        }
    }
}

/* This button has a square background and a text on it */

class TextButton( render_surface : Panel,
    x : Int, y : Int, width : Int, height : Int,
    text : String )
extends TDButton( render_surface, x, y, width, height )
{
    var color = Colors.red
    var text_color = Colors.white
    override def draw_contents(g: Graphics2D) : Unit = {
        g.setColor( color )
        g.fill( surface )
        g.setColor( text_color )
        val stringwidth = g.getFontMetrics().stringWidth( text )
        g.drawString( text,
            x + width / 2 - stringwidth / 2,
            y + height / 2 )
    }
}
