package game_mechanics

import game_mechanics._
import scala.collection.mutable._

class one_round(player:Player) { 

  def one_round(Bl : Buffer[Bunny], Bt : Buffer[Tower], Bth : Buffer[Throw]): Unit
  /**
  * One round of the game_loop
  */
  = {
    /* Remove all dead rabbits */
    for (bun <- Bl.iterator ) {
      if (bun.hp <= 0) {
        Bl -= bun
        player.gold += bun.reward
      }
    }
    /* remove all throws that hit */
    for (vegetable <- Bth.iterator) {
      if (vegetable.pos == vegetable.target.pos) {
        Bth -= vegetable
      }
    }
    for ( tower <- Bt.iterator) {
      if (tower.to_sell) {
        Bt -= tower
        player.gold += tower.sell_cost
      }
    }
  }
}
