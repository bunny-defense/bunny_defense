
package tcp.packets

case class PlayerIdPacket(player_id: Int)
extends Packet

object PlayerIdPacket
{
    def serialize() : Array[Byte] = {
        throw new Exception("Not implemented")
    }
    def deserialize(data: Array[Byte]) : PlayerIdPacket = {
        throw new Exception("Not implemented")
    }
}
