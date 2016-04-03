
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

  def ++=(other:Path): Unit = {
    this.waypoints ++= other.waypoints
  }

  /* Returns the waypoint of index i */
  def at(i: Int): Waypoint = {
    return waypoints.apply(i)
  }
  def length(): Int = {
    return waypoints.length
  }
  def last(): Waypoint = {
    return waypoints.last
  }
  def reverse(): Path = {
    val npath = new Path()
    npath.waypoints.appendAll( this.waypoints.reverse )
    return npath
  }

  def exists(p:(Waypoint) => Boolean) : Boolean = {
      this.waypoints.exists(p)
  }
}
