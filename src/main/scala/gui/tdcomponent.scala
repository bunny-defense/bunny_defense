
package gui

import swing._
import collection.mutable.ListBuffer

import java.awt.Graphics2D

import runtime.StateManager
import game_mechanics.path.CellPos

class TDComponent
{
    var pos = new CellPos( 0, 0 )
    def set_pos( posx: Int, posy: Int ) : Unit = {
        pos = new CellPos( posx, posy )
    }

    var size : CellPos = new CellPos( 0, 0 )
    val children = new ListBuffer[TDComponent]()

    def update(dt: Double) : Unit = {}
    def draw(g: Graphics2D) : Unit = {
        children.foreach( _.draw(g) )
    }
    def on_click(posx: Int, posy: Int) : Unit = {}
    def locationOnScreen : CellPos = {
        val loc = StateManager.render_surface.locationOnScreen
        return new CellPos(
            loc.x + pos.x,
            loc.y + pos.y )
    }
    /*
    def +=(component: TDComponent) : Unit = {
        children += component
    }
    */
}
