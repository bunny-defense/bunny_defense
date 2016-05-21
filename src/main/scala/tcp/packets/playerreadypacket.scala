
package tcp.packets

case class PlayerReadyPacket(state: Boolean)
extends Packet

object PlayerReadyPacket
{
    def serialize() : Array[Byte] = {
        throw new Exception("Not implemented")
    }
    def deserialize(data: Array[Byte]) : PlayerReadyPacket = {
        throw new Exception("Not implemented")
    }
}
