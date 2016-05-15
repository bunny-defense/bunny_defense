
package gui

import collection.mutable.ListBuffer

import java.awt.Graphics2D
import java.awt.MouseInfo

import swing.{Panel, Rectangle}

import runtime.StateManager
import runtime.TowerDefense
import game_mechanics.path.CellPos

class MainMenu(parent: Option[TDComponent])
extends TDComponent(parent)
{
    class WideButton( y : Int, text : String )
    extends TextButton( Some(this), text )
    {
        pos  = new CellPos( (TowerDefense.gui_size.width * 0.25).toInt, y )
        size = new CellPos( (TowerDefense.gui_size.width * 0.50).toInt, 50 )
    }

    val background_color = Colors.black
    override def draw(g: Graphics2D) : Unit = {
        g.setColor( background_color )
        g.fillRect( 0, 0,
            StateManager.render_surface.size.width,
            StateManager.render_surface.size.height )
        super.draw(g)
    }

    def +=(component: TDComponent) : Unit = {
        children += component
    }
}
