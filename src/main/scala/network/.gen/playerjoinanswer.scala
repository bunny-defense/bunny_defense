package network.packets

object PlayerJoinAnswer
{
    def unserialize(data: Array[Byte]): PlayerJoinAnswer = {
    }
}

class PlayerJoinAnswer(player_id: Int)
extends Packet
{
    def serialize(): Array[Byte] = {
        Serialize.int(player_id)
    }
}