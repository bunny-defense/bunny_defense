package network.packets

object PlayerJoinRequest
{
    def unserialize(data: Array[Byte]): PlayerJoinRequest = {
    }
}

class PlayerJoinRequest(player_name: String)
extends Packet
{
    def serialize(): Array[Byte] = {
        Serialize.string(player_name)
    }
}