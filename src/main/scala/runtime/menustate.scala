
package runtime

import swing._
import swing.event._

import gui.MainMenu
import gui.TDTextField
import game_mechanics.path.CellPos
import tcp._

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
    new gui.WideButton( 50, "Play" )
    {
        override def action() : Unit = {
            StateManager.set_state( new PlayMenuState() )
        }
    }
    new gui.WideButton( 120, "Quit" )
    {
        override def action() : Unit = {
            TowerDefense.quit()
        }
    }
}

class PlayMenuState extends MenuState
{
    new gui.WideButton( 50, "Singleplayer" )
    {
        override def action() : Unit = {
            StateManager.set_state( TowerDefense.gamestate)
        }
    }
    new gui.WideButton( 120, "Multiplayer" )
    {
        override def action() : Unit = {
            StateManager.set_state( new MultiplayerMenuState() )
        }
    }
    new gui.WideButton( 190, "Back" )
    {
        override def action() : Unit = {
            StateManager.set_state( new MainMenuState() )
        }
    }
}

class MultiplayerMenuState extends MenuState
{
    new gui.WideButton( 50, "Join" )
    {
        override def action() : Unit = {
            StateManager.set_state( new Lobby() )
        }
    }
    new gui.WideButton( 120, "Host & Play" )
    {
        override def action() : Unit = {
            val serverthread = new Server()
            //StateManager.set_state( new NumberOfPlayerState() )
        }
    }

    new gui.WideButton( 190, "Host" )
    {
        override def action() : Unit = {
            val serverthread = new Server()
        }
    }
    new gui.WideButton( 260, "Back" )
    {
        override def action() : Unit = {
            StateManager.set_state( new PlayMenuState() )
        }
    }
}

/*
class NumberOfPlayerState extends MenuState
{
    new gui.WideButton( 50, "2 Player")
    {
        override def action() : Unit = {
            val clienthreads = List(
                new ClientThread("localhost"),
                new ClientThread("localhost")
            )
        }
    }
    new gui.WideButton( 120, "3 player")
    {
        override def action() : Unit = {
            val clienthreads = List(
                new ClientThread("localhost"),
                new ClientThread("localhost"),
                new ClientThread("localhost")
            )
        }
    }
}
*/

class Lobby extends MenuState
{
    new gui.WideButton( 50, "Back" )
    {
        override def action() : Unit = {
            StateManager.set_state( new PlayMenuState() )
        }
    }
    new TDTextField(Some(gui))
    {
        pos         = new CellPos( 50, 50 )
        size        = new CellPos( 400, 50 )
        placeholder = "Host name"
    }
}
