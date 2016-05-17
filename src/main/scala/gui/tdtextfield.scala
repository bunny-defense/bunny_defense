
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

    override def on_event(event: Event) : Unit = {
        event match {
            case e : KeyPressed => e.key match {
                case Key.BackSpace =>
                {
                    if( focused )
                        text = text.dropRight(1)
                }
                case Key.Escape =>
                    focused = false
                case Key.Enter =>
                    if( focused )
                        on_enter()
                case _ => {}
            }
            case e : KeyTyped =>
            {
                if( e.char >= 0x20 && e.char <= 0x7E && focused )
                    text += e.char
            }
            case _: MouseReleased =>
                focused = false
            case _ => {}
        }
        super.on_event(event)
    }
    override def action() : Unit = {
        focused = true
    }
    def on_enter() : Unit = {}
    override def draw_contents(g: Graphics2D) : Unit = {
        g.setColor( background_color )
        if( focused )
            g.setColor( Colors.darkgreen )
        g.fillRect( 0, 0, size.x, size.y )
        if( text == "" )
        {
            g.setColor(Colors.lightGrey)
            g.drawString( placeholder,
                10, size.y / 2 )
        }
        g.setColor(text_color)
        g.drawString( text + "_",
            10, size.y / 2 )
    }
}
