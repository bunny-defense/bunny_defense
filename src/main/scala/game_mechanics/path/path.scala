
package game_mechanics.path

import collection.mutable.Stack

class Path {
  /** A Path is a list of waypoints */
  var waypoints = new Stack[Waypoint]()

  def Path( p:Path ) = {
    waypoints = p.waypoints.clone()
  }
  def +=(wp: Waypoint): Unit = {
    waypoints.push(wp)
  }

  def ++=(other:Path): Unit = {
    this.waypoints.pushAll(other.waypoints)
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

  def pop(): Waypoint = {
    return waypoints.pop
  }

  def head(): Waypoint = {
    return waypoints.head
  }

  def apply(i: Int): Waypoint = {
    return waypoints.apply(i)
  }

  def push(wp: Waypoint): Unit = {
    waypoints.push(wp)
  }

  def pushAll(p: Path): Unit = {
      waypoints.pushAll(p.waypoints)
  }

  def takeRight(i: Int): Stack[Waypoint] =
    return this.waypoints.takeRight(i)

  def reverse(): Path = {
    val npath = new Path()
    npath.waypoints.pushAll( this.waypoints)
    return npath
  }

  def exists(p:(Waypoint) => Boolean) : Boolean = {
      this.waypoints.exists(p)
  }

  override def toString() : String = {
      this.waypoints.toString
  }

  def isEmpty(): Boolean = {
      return waypoints.isEmpty
  }

}
