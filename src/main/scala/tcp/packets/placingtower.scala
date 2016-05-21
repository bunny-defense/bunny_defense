
package tcp.packets

import game_mechanics.path.CellPos

case class PlacingTower(towertype: Int, pos: CellPos)
extends Packet

object PlacingTower
{
    def serialize() : Array[Byte] = {
        throw new Exception("Not implemented")
    }
    def deserialize(data: Array[Byte]) : PlacingTower = {
        throw new Exception("Not implemented")
    }
}
