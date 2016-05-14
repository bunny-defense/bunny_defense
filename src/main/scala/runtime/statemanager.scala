
package runtime

import swing._
import swing.event._


/** This is an empty state where nothing happens **/
class VoidState extends State
{
    override def update(dt: Double) : Unit = {}
    override def render(g: Graphics2D) : Unit = {}
}

/** Manages the states **/
object StateManager extends Reactor
{
    var current_state : State = new VoidState()
    val render_surface = new Panel() {
        preferredSize = TowerDefense.gui_size
        override def paintComponent(g: Graphics2D) : Unit = {
            super.paintComponent(g)
            StateManager.current_state.render(g)
        }
    }
    listenTo( render_surface.mouse.clicks )
    reactions += {
        case MouseClicked(_,pos,_,_,_) =>
        {
            current_state.on_click(pos.x, pos.y)
        }
    }
    /* Changes the current state and discards the old one */
    def set_state(new_state: State) : Unit = {
        current_state = new_state
    }
    /* Main game loop, runs the loop with the
        render and update functions provided by the state object */
    def run() : Unit = {
        var dt : Double = 0
        while( true )
        {
            val start = System.currentTimeMillis
            /* Update */
            current_state.update(dt)
            /* Render */
            render_surface.repaint()
            val milliseconds = TowerDefense.framerate.toInt
                - (System.currentTimeMillis - start)
            if( milliseconds < 0 )
                println( "Can't keep up!" )
            Thread.sleep(Math.max(0, milliseconds))
            dt = (System.currentTimeMillis - start).toDouble / 1000
        }
    }
}
