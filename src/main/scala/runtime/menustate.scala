
package runtime

import swing._
import swing.event._

import gui.MainMenu
import gui.TDTextField
import gui.TDButton
import gui.TextButton
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

object MainMenuState extends MenuState
{
    new gui.WideButton( 50, "Play" )
    {
        override def action() : Unit = {
            StateManager.set_state( PlayMenuState )
        }
    }
    new gui.WideButton( 120, "Quit" )
    {
        override def action() : Unit = {
            TowerDefense.quit()
        }
    }
}

object PlayMenuState extends MenuState
{
    new gui.WideButton( 50, "Singleplayer" )
    {
        override def action() : Unit = {
            //StateManager.set_state( new ClientServerGameState() )
        }
    }
    new gui.WideButton( 120, "Multiplayer" )
    {
        override def action() : Unit = {
            StateManager.set_state( MultiplayerMenuState )
        }
    }
    new gui.WideButton( 190, "Back" )
    {
        override def action() : Unit = {
            StateManager.set_state( MainMenuState )
        }
    }
}

object MultiplayerMenuState extends MenuState
{
    new gui.WideButton( 50, "Join" )
    {
        override def action() : Unit = {
            StateManager.set_state( new ServerConnectionMenu() )
        }
    }
    new gui.WideButton( 120, "Host & Play" )
    {
        override def action() : Unit = {
            //StateManager.set_state( new ClientServerGameState() )
        }
    }

    new gui.WideButton( 190, "Host" )
    {
        override def action() : Unit = {
            StateManager.set_state( new ServerLobby() )
        }
    }
    new gui.WideButton( 260, "Back" )
    {
        override def action() : Unit = {
            StateManager.set_state( PlayMenuState )
        }
    }
}

class ServerConnectionMenu extends MenuState
{
    def connect() : Unit = {
        if (!(hostname_field.text == "")) {
            val connection = new ClientThread(hostname_field.text)
            connection.start()
            StateManager.set_state( new ClientLobby(connection) )
        }
    }
    val hostname_field = new TDTextField(Some(gui))
    {
        pos         = new CellPos( TowerDefense.gui_size.width / 4, 50 )
        size        = new CellPos( TowerDefense.gui_size.width / 2, 50 )
        placeholder = "Host name"
        override def on_enter() : Unit = {
            connect()
        }
    }
    new gui.WideButton( 120, "Connect")
    {
        override def action() : Unit = {
            connect()
        }
    }
    new gui.WideButton( 260, "Back" )
    {
        override def action() : Unit = {
            StateManager.set_state( MultiplayerMenuState )
        }
    }
}
