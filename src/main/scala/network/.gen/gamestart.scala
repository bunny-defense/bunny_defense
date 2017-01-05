package network.packets

object GameStart
{
    def unserialize(data: Array[Byte]): GameStart = {
    }
}

class GameStart(map: Array[Cell])
extends Packet
{
    def serialize(): Array[Byte] = {
        Serialize.array[Cell](map)
    }
}