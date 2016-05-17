
package runtime

import gui._
import tcp._

class Lobby extends MenuState
{
    new gui.WideButton( TowerDefense.gui_size.height - 100, "Back" )
    {
        override def action() : Unit = {
            StateManager.set_state( PlayMenuState )
        }
    }
}

class ServerLobby extends Lobby
{
    val list = new TDTextList(Some(gui))
    val server = new Server()
    {
        override def on_connect(peer: ServerThread) : Unit = {
            println( "Client connected !" )
            list += peer.player_name
        }
        override def on_disconnect(peer: ServerThread) : Unit = {
            println( "Client disconnected !" )
            list -= peer.player_name
        }
    }
}

class ClientLobby(connection: ClientThread) extends Lobby
