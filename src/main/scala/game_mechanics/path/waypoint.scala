
package game_mechanics.path

object Waypoint {
  def distance( a: Waypoint, b: Waypoint ): Double = {
    return math.sqrt(
      math.pow( a.x - b.x, 2 )
        + math.pow( a.y - b.y, 2 ) )
  }
}

class Waypoint(x0: Double, y0: Double) {
  val x = x0
  val y = y0

  def this() { this(0,0) }

  def +(other: Waypoint): Waypoint = {
    return new Waypoint( x + other.x, y + other.y )
  }
  def *(scalar: Double): Waypoint = {
    return new Waypoint( x * scalar, y * scalar )
  }
  def /(scalar: Double): Waypoint = {
    return new Waypoint( x / scalar, y / scalar )
  }

}
