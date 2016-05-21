
package tcp.packets

case class PlayerInfoPacket(name : String)
extends Packet

object PlayerInfoPacket
{
    def serialize() : Array[Byte] = {
        throw new Exception("Not implemented")
    }
    def deserialize(data: Array[Byte]) : PlayerInfoPacket = {
        throw new Exception("Not implemented")
    }
}
