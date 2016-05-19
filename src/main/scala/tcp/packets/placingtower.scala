
package tcp.packets

import game_mechanics.path.CellPos

case class PlacingTower(towertype: Int, pos: CellPos)
extends Packet
