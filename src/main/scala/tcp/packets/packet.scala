
package tcp.packets

object Packet
{
    val PLAYER_ID_PACKET     = 1
    val PLAYER_READY_PACKET  = 2
    val PLAYER_INFO_PACKET   = 3
    val GAME_START_PACKET    = 4
    val PLACING_TOWER_PACKET = 5
    val PLACED_TOWER_PACKET   = 6
    def apply(data: Array[Byte]) : Packet = {
        data(0) match {
            case PLAYER_ID_PACKET =>
                PlayerIdPacket.deserialize(data)
            case PLAYER_READY_PACKET =>
                PlayerReadyPacket.deserialize(data)
            case PLAYER_INFO_PACKET =>
                PlayerIdPacket.deserialize(data)
            case GAME_START_PACKET =>
                PlayerIdPacket.deserialize(data)
            case PLACING_TOWER_PACKET =>
                PlayerIdPacket.deserialize(data)
            case PLACED_TOWER_PACKET =>
                PlayerIdPacket.deserialize(data)
        }
    }
}
abstract class Packet
