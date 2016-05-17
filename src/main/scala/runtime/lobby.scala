
package runtime

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
    val server = new Server()
    {
        override def on_connect(peer: ServerThread) : Unit = {
            println( "Client connected !" )
        }
        override def on_disconnect(peer: ServerThread) : Unit = {
            println( "Client disconnected !" )
        }
    }
}

class ClientLobby(connection: ClientThread) extends Lobby
