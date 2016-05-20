
package runtime

import gui._
import game_mechanics.path.CellPos
import tcp._
import tcp.packets._

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
    {
        pos = new CellPos( TowerDefense.gui_size.width / 2, 50 )
    }
    list += "Players"
    val server = new Server()
    {
        override def on_connect(peer: ServerThread) : Unit = {
            println( "Client connected !" )
            list += peer.player.name
        }
        override def on_disconnect(peer: ServerThread) : Unit = {
            println( "Client disconnected !" )
            list -= peer.player.name
        }
    }
}

class ClientLobby(hostname: String, name : String) extends Lobby
{
    val connection = new ClientThread(hostname, name)
    connection.start()
    new TDToggleButton(Some(gui), "Ready")
    {
        pos  = new CellPos( 10, 10 )
        size = new CellPos( 100, 50 )
        override def action() : Unit = {
            super.action()
            connection.send(PlayerReadyPacket(toggled))
        }
    }
}
