
package runtime

import swing._
import swing.event._

import gui.MainMenu

/** Represents a menu state, for a specific menu **/
abstract class MenuState extends State
{
    val gui = new MainMenu(None)

    override def update(dt: Double) : Unit = {
    }
    override def render(g: Graphics2D) : Unit = {
        gui.draw(g)
    }
    override def on_event(event: Event) : Unit = {
        gui.on_event(event)
    }
}

class MainMenuState extends MenuState
{
    gui += new gui.WideButton( 50, "Play" )
    {
        override def action() : Unit = {
            StateManager.set_state( new PlayMenuState() )
        }
    }
    gui += new gui.WideButton( 120, "Quit" )
    {
        override def action() : Unit = {
            TowerDefense.quit()
        }
    }
}

class PlayMenuState extends MenuState
{
    gui += new gui.WideButton( 50, "Singleplayer" )
    {
        override def action() : Unit = {
            StateManager.set_state( TowerDefense.gamestate )
        }
    }
    gui += new gui.WideButton( 120, "Multiplayer" )
    {
        override def action() : Unit = {
            StateManager.set_state( new MultiplayerMenuState() )
        }
    }
    gui += new gui.WideButton( 190, "Back" ) {
        override def action() : Unit = {
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
        override def action() : Unit = {
            StateManager.set_state( new PlayMenuState() )
        }
    }
}
