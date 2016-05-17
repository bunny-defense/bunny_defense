
package gui

import collection.mutable.ArrayBuffer

import java.awt.Graphics2D

class TDTextList(parent: Option[TDComponent])
extends TDComponent(parent)
{
    var color = Colors.white
    val text_list = new ArrayBuffer[String]
    override def draw(g: Graphics2D)
    {
        super.draw(g)
        g.setColor(color)
        for( index <- 0 until text_list.size )
        {
            val text = text_list(index)
            val center = g.getFontMetrics().stringWidth( text ) / 2
            g.drawString( text,
                pos.x - center,
                pos.y + 20 * index )
        }
    }
    def +=(text: String) : Unit = {
        text_list += text
    }
    def -=(text: String) : Unit = {
        text_list -= text
    }
}
