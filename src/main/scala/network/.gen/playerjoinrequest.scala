package network.packets

object PlayerJoinRequest
{
    def unserialize(data: Array[Byte]): PlayerJoinRequest
    {
    }
}

class PlayerJoinRequest(player_name: String)
extends Packet
{
    def serialize(player_name: String): Array[Byte]
    {
        Serialize.string(player_name)
    }
}