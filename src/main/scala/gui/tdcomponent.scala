
package gui

import swing._
import swing.event._
import collection.mutable.ListBuffer

import java.awt.Graphics2D

import runtime.StateManager
import game_mechanics.path.CellPos

class TDComponent(parent: Option[TDComponent])
{
    parent match {
        case Some(component) =>
            component.children += this
        case None => {}
    }
    private val self = this
    var pos = new CellPos( 0, 0 )
    def set_pos( posx: Int, posy: Int ) : Unit = {
        pos = new CellPos( posx, posy )
    }

    var size : CellPos = new CellPos( 0, 0 )
    val children = new ListBuffer[TDComponent]()

    def update(dt: Double) : Unit = {}
    def draw(g: Graphics2D) : Unit = {
        children.foreach(
            {
                (child: TDComponent) =>
                val transform = g.getTransform()
                g.translate( child.pos.x, child.pos.y )
                child.draw(g)
                g.setTransform( transform )
            }
        )
    }
    def locationOnScreen : CellPos = {
        parent match {
            case Some(component) =>
            {
                return component.locationOnScreen + pos
            }
            case None =>
            {
                val loc = StateManager.render_surface.locationOnScreen
                return new CellPos( loc.x + pos.x, loc.y + pos.y )
            }
        }
    }
    def on_event( event: Event ) : Unit = {
        children.foreach( _.on_event(event) )
    }
}
