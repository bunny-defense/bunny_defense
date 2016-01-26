
package game_mechanics.path

import collection.mutable.ListBuffer

class Path {
  val wps = new ListBuffer[Waypoint]()
  var progress = 0.0
  def Path( p:Path ) {
    for( wp <- p.wps ) {
      this.add( wp )
    }
  }
  def add(wp: Waypoint): Unit = {
    wps += wp
  }
}
