
package tcp.packets

import game_mechanics.path.CellPos

case class PlacedTower(towertype: Int, pos: CellPos, player_id: Int)
extends Packet

object PlacedTower
{
    def serialize() : Array[Byte] = {
        throw new Exception("Not implemented")
    }
    def deserialize(data: Array[Byte]) : PlacedTower = {
        throw new Exception("Not implemented")
    }
}
