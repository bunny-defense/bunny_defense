package network.packets

object GameStart
{
    def unserialize(data: Array[Byte]): GameStart
    {
    }
}

class GameStart(map: Array[Cell])
extends Packet
{
    def serialize(map: Array[Cell]): Array[Byte]
    {
        Serialize.array[cell](map)
    }
}