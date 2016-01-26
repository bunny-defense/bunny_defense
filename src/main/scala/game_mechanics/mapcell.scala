
package game_mechanics

/* The cell for the game mechanics, it can be walkable */
class MapCell(x:Int, y:Int) {

  var walkable = true

  def UpgradeCell(new_value: Boolean): Unit = {
    this.walkable = new_value
  }

}
