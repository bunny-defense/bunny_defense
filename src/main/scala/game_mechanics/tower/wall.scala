
package game_mechanics.tower

import game_mechanics.Bunny

object Wall extends TowerType {

    override val buy_cost = 5

    override def fire_from(tower: Tower)(bunny: Bunny) {}

}
