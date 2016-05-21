
package tcp.packets

import collection.mutable.ListBuffer

import game_mechanics.GameMap
import game_mechanics.Player
import game_mechanics.path.CellPos

case class GameStartPacket(
    map: Array[Array[Boolean]],
    players: ListBuffer[(Int,String,CellPos)])
extends Packet

object GameStartPacket
{
    def serialize() : Array[Byte] = {
        throw new Exception("Not implemented")
    }
    def deserialize(data: Array[Byte]) : GameStartPacket = {
        throw new Exception("Not implemented")
    }
}
