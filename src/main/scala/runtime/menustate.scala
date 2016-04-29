
package runtime

import swing._

import gui.MainMenu

class MenuState extends State
{
    val gui = new MainMenu( StateManager.render_surface )
    override def update(dt: Double) : Unit = {
    }
    override def render(g: Graphics2D) : Unit = {
        gui.render(g)
    }
}
