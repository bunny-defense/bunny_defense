
package game_mechanics.path

import collection.mutable.ListBuffer

class Path {
  val wps = new ListBuffer[Waypoint]()
  def Path( p:Path ) = {
    for( wp <- p.wps ) {
      this.add( wp )
    }
  }
  def add(wp: Waypoint): Unit = {
    wps += wp
  }
  def at(i: Int): Waypoint = {
    return wps.apply(i)
  }
  def length(): Int = {
    return wps.length
  }
}
