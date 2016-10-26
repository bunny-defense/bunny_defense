
package gui

import java.awt.Graphics2D

/* A simple label component holding text */
class TDLabel(parent: Option[TDComponent], text: String)
extends TDComponent(parent)
{
    var color = Colors.white
    override def draw(g: Graphics2D) : Unit = {
        super.draw(g)
        val center = g.getFontMetrics().stringWidth( text ) / 2
        g.setColor( color )
        g.drawString( text,
            -center, 0 )
    }
}
