
package runtime

import swing._

abstract class State
{
    def update(dt: Double) : Unit
    def render(g: Graphics2D) : Unit
}

class VoidState extends State
{
    override def update(dt: Double) : Unit = {}
    override def render(g: Graphics2D) : Unit = {}
}

object StateManager
{
    var current_state : State = new VoidState()
    val render_surface = new Panel() {
        preferredSize = TowerDefense.gui_size
        override def paintComponent(g: Graphics2D) : Unit = {
            super.paintComponent(g)
            StateManager.current_state.render(g)
        }
    }
    def set_state(new_state: State) : Unit = {
        current_state = new_state
    }
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
