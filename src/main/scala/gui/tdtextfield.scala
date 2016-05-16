
package gui

import swing.Reactor
import swing.event._

import java.awt.Graphics2D

import runtime.StateManager

class TDTextField(parent: Option[TDComponent])
extends TDButton(parent) with Reactor
{
    var background_color = Colors.darkred
    var text_color       = Colors.white
    var placeholder      = "Type here"
    var text             = ""
    var focused          = false
    listenTo( StateManager.render_surface.keys )
    reactions += {
        case e: KeyTyped =>
            text += e.char
    }
    override def on_event(event: Event) : Unit = {
        event match {
            case _: MouseReleased =>
                focused = false
        }
        super.on_event(event)
    }
    override def action() : Unit = {
        focused = true
    }
    override def draw(g: Graphics2D) : Unit = {
        super.draw(g)
        g.setColor( background_color )
        g.fillRect( 0, 0, size.x, size.y )
        g.setColor(text_color)
        var print_text = text
        if( text == "" )
        {
            g.setColor(Colors.lightGrey)
            print_text = placeholder
        }
        g.drawString( print_text,
            10, 10 )
    }
}
