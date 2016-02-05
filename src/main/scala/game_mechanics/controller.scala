package game_mechanics


def one_round(Bl : Buffer[Bunny], Bt : Buffer[Tower], Bth : Buffer[Throw]): Unit
  /**
  * One round of the game_loop
  */
= {
  /* Remove all dead rabbits */
  for (bun in Bl.iterator() ) {
    if (bun.hp <= 0) {
      Bl -= bun
    }
  }
  /* remove all throws that hit */
  for (vegetable in Bth.iterator()) {
    if (vegetable.pos == vegetable.target.pos) {
      Bth -= vegetable
    }
  }
  for ( tower in Bt.iterator()) {
    if (tower.to_sell) {
      Bth -= tower
      player.gold += tower.sell_cost
    }
  }
}
