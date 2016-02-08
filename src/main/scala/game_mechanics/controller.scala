package game_mechanics

import game_mechanics._
import scala.collection.mutable.ListBuffer

class one_round(player:Player) {

  def one_round(Bl : ListBuffer[Bunny],
                Bt : ListBuffer[Tower],
                Bth : ListBuffer[Throw]): Unit
  /**
  * One round of the game_loop
  * First step is to update the lists of rabbits, 
  */
  = {
    /* First step, update the whole game */
    /* Remove all dead rabbits */
    for (bun <- Bl.iterator ) {
      if (bun.hp <= 0) {
        Bl -= bun
        player.gold += bun.reward
      }
    }
    /* remove all vegetables that hit */
    for (vegetable <- Bth.iterator) {
      if (vegetable.pos == vegetable.target.pos) {
        Bth -= vegetable
      }
    }
    /* remove all sold towers */
    for ( tower <- Bt.iterator) {
      if (tower.to_sell) {
        Bt -= tower
        player.gold += tower.sell_cost
      }
    }

    /* Now check if some HP were lost. */
    for (bunny <- Bl.iterator) {
      if (bunny.path.reached) {
        player.remove_hp
        Bl -= bunny
      }
    /* Now we can make all rabbits move */
    for (bunny <- Bl.iterator) {
      bunny.move(dt)
    }

  }
}
