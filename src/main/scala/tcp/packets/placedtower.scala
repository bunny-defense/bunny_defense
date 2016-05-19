
package tcp.packets

import game_mechanics.path.CellPos

case class PlacedTower(towertype: Int, pos: CellPos, player_id: Int)
extends Packet
