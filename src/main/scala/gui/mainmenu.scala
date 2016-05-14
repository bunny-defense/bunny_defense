
package gui

import collection.mutable.ListBuffer

import java.awt.Graphics2D
import java.awt.MouseInfo

import swing.{Panel, Rectangle}

import runtime.TowerDefense

class MainMenu( render_surface : Panel )
{
    class WideButton( y : Int, text : String )
    extends TextButton( render_surface,
        (TowerDefense.gui_size.width * 0.25).toInt, y,
        (TowerDefense.gui_size.width * 0.50).toInt, 50, text )
    {
    }

    val background_color = Colors.black
    private val buttonset = new ListBuffer[TDButton]()

    def render(g: Graphics2D) : Unit = {
        val surface = new Rectangle(
            0, 0,
            TowerDefense.gui_size.width,
            TowerDefense.gui_size.height )
        g.setColor( background_color )
        g.fill( surface )
        for( button <- buttonset )
        {
            button.draw(g)
        }
    }

    def on_click(posx: Int, posy: Int) : Unit = {
        buttonset.foreach( _.on_click(posx, posy) )
    }

    def +=( button : TDButton ) : Unit = {
        buttonset += button
    }
}
