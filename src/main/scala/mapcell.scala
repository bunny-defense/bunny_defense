
import game_mechanism

import runtime

/* The cell for the game mechanism, it can be walkable */

class MapCell(x:Int, y:Int) {

  this.walkable = true

  def UpgradeCell(new: Boolean) Unit = {
    this.walkable = new
  }

}
