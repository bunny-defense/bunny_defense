trait ProgressTrait {
  var i: Int
  /* The number of waypoints of the path walked so far */
  var progress: Double
  /* The distance walked since the last waypoint */
  val path: Path
  /* The path considered */
  def move( distance: Double ): Unit
  /* Returns the current actual position of the bunny (taking progress into account) */
}
