trait Waypoint_object { /* WARNING : Name different from object name in waypoint.scala ! */
  def distance( a: Waypoint, b: Waypoint ): Double
  /* Do you really need a comment for this one? */

}

trait WaypointTrait {
  var x: Double
  var y: Double
  /* The coordinates of the waypoint */
  def this: ()Waypoint
  /* No argument, returns an "empty" waypoint (coordinates (0,0)) */
  def +(other: Waypoint): Waypoint
  /* Returns the waypoint with coordinates sum of those if this waypoint, and of other */
  def -(other: Waypoint): Waypoint
  /* Same as before, but substracts other to this waypoint */
  def +=(other: Waypoint): Waypoint
  /* Same as before, but also updates the corrdinates of this waypoint */
  def *(scalar: Double): Waypoint
  /* Returns a waypoint with coordinates equal to those of this waypoint multiplied by scalar */
  def /(scalar: Double): Waypoint
  /* Same as before, but divided by scalar */
}
