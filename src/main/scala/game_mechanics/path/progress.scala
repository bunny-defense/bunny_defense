
package game_mechanics.path

class Progress(p: Path) {
  var progress = 0.0
  var current = Waypoint
  val path = p
  val iterator = p.wps.iterator
  def move( distance: Double ): Unit = {

  }
}
