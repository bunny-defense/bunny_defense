
package runtime

import swing._

import gui.MainMenu

/** Represents a menu state, for a specific menu **/
abstract class MenuState extends State
{
    listenTo( StateManager.render_surface.mouse.clicks )
    val gui = new MainMenu( StateManager.render_surface )

    override def update(dt: Double) : Unit = {
    }
    override def render(g: Graphics2D) : Unit = {
        gui.render(g)
    }
    override def on_click(posx: Int, posy: Int) : Unit = {
        gui.on_click(posx, posy)
    }
}

class MainMenuState extends MenuState
{
    gui += new gui.WideButton( 50, "Play" )
    {
        override def on_click() : Unit = {
            StateManager.set_state( new PlayMenuState() )
        }
    }
    gui += new gui.WideButton( 120, "Quit" )
    {
        override def on_click() : Unit = {
            TowerDefense.quit()
        }
    }
}

class PlayMenuState extends MenuState
{
    gui += new gui.WideButton( 50, "Singleplayer" )
    {
        override def on_click() : Unit = {
            StateManager.set_state( TowerDefense.gamestate )
        }
    }
    gui += new gui.WideButton( 120, "Multiplayer" )
    {
        override def on_click() : Unit = {
            StateManager.set_state( new MultiplayerMenuState() )
        }
    }
    gui += new gui.WideButton( 190, "Back" ) {
        override def on_click() : Unit = {
            StateManager.set_state( new MainMenuState() )
        }
    }
}

class MultiplayerMenuState extends MenuState
{
    gui += new gui.WideButton( 50, "Join" )
    gui += new gui.WideButton( 120, "Host & Play" )
    gui += new gui.WideButton( 190, "Host" )
    gui += new gui.WideButton( 260, "Back" )
    {
        override def on_click() : Unit = {
            StateManager.set_state( new PlayMenuState() )
        }
    }
}
