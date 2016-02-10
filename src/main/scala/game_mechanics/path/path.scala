
package game_mechanics.path

import collection.mutable.ListBuffer

/* a Path is a list of waypoints */
class Path {
  val waypoints = new ListBuffer[Waypoint]()

  def Path( p:Path ) = {
    for( wp <- p.waypoints ) {
      waypoints += wp
    }
  }
  def +=(wp: Waypoint): Unit = {
    waypoints += wp
  }

  /* Returns the waypoint of index i */
  def at(i: Int): Waypoint = {
    return waypoints.apply(i)
  }
  def length(): Int = {
    return waypoints.length
  }
}
