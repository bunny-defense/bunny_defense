
package game_mechanics.path

class Progress(p: Path) {
  var i = 1
  var progress = 0.0
  val path = p
  def move( distance: Double ): Unit = {
    val current = path.at(i-1)
    val next = path.at(i)
    val dist = Waypoint.distance( current, next )
    if( (dist - progress) < distance ) {
      progress = 0.0
      i += 1
      move( distance - (dist - progress) )
    }
    else {
      progress += distance
    }
  }
  def get_position(): Waypoint = {
    val current = path.at(i-1)
    val next = path.at(i)
    val ratio = progress / Waypoint.distance( current, next )
    return (current + next) / ratio
  }
}
