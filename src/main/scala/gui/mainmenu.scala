
package gui

import collection.mutable.ListBuffer

import java.awt.Graphics2D
import java.awt.MouseInfo

import swing.{Panel, Rectangle}

import runtime.TowerDefense
import game_mechanics.path.CellPos

class MainMenu( render_surface : Panel ) extends TDComponent
{
    class WideButton( y : Int, text : String )
    extends TextButton( render_surface, text )
    {
        pos  = new CellPos( (TowerDefense.gui_size.width * 0.25).toInt, y )
        size = new CellPos( (TowerDefense.gui_size.width * 0.50).toInt, 50 )
    }

    val background_color = Colors.black
    override def draw(g: Graphics2D) : Unit = {
        g.setColor( background_color )
        g.fillRect( 0, 0,
            render_surface.size.width, render_surface.size.height )
        super.draw(g)
    }

    def +=(component: TDComponent) : Unit = {
        children += component
    }
}
