
package path

import collection.mutable.ListBuffer


object Waypoint {
  def distance( a: Waypoint, b: Waypoint ): Double = {
    return math.sqrt(
      math.pow( a.x - b.x, 2 )
        + math.pow( a.y - b.y, 2 ) )
  }
}

class Waypoint {
  val x = 0
  val y = 0
}

class Path {
  val wps = new ListBuffer[Waypoint]()
  val progress = 0.
  def Path( p:Path ) {
    for( wp <- p.wps ) {
      this.add( wp )
    }
  }
  def add(wp: Waypoint): Unit = {
    wps += wp
  }
}
