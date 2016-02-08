package game_mechanics

import game_mechanics._
import scala.collection.mutable.ListBuffer
val break_loop = new Break()

import break_loop.{break,breakable}

class one_round(player:Player) {

  def one_round(Bl : ListBuffer[Bunny],
                Bt : ListBuffer[Tower],
                Bv : ListBuffer[Throw]): Unit
  /**
  * One round of the game_loop
  * First step is to update the lists of rabbits,
  */
  = breakable {
    /* First step, update the whole game */
    /* Remove all dead rabbits */
    for (bunny <- Bl.iterator ) {
      if (bunny.hp <= 0) {
        Bl -= bunny
        player.gold += bunny.reward
      }
    }
    /* Now check if some HP were lost. */
    for (bunny <- Bl.iterator) {
      if (bunny.path.reached) {
        player.remove_hp
        Bl -= bunny
      }
    /* If nobody lasts, the round is over */
    if (Bl.isEmpty && spawn_scheduler.isEmpty) {
      break()
    }
    /* remove all vegetables that hit */
    for (vegetable <- Bv.iterator) {
      if (vegetable.pos == vegetable.target.pos) {
        Bth -= vegetable
        vegetable.target.take_damage(vegetable.damage)
      }
    }
    /* remove all sold towers */
    for ( tower <- Bt.iterator) {
      if (tower.to_sell) {
        Bt -= tower
        player.gold += tower.sell_cost
      }
    }

    /* New bunnies can appear */
    while ((spawn_scheduler.appl(0)).time <= System.nanotime - begin_time) {
      Bl += spawn_scheduler.apply(0).bunny
      spawn_scheduler.head
    }
    /* Now we can make all rabbits move */
    for (bunny <- Bl.iterator) {
      bunny.move(dt)
    }
    /* All towers try to attack */
    for (tower <- Bt.iterator) {
      tower.update(dt)
    }
    /* All vegetable continue their movements */
    for (vegetable <- Bv.iterator) {
      vegetable.progress(dt)
    }
  }
}
