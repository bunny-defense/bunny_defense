trait ProgressTrait {
  var i: Int
  /* The number of waypoints of the path walked so far */
  var progress: Double
  /* The distance walked since the last waypoint */
  val path: Path
  /* The path considered */
  def move( distance: Double ): Unit
  /* Makes the bunny walk the given distance. If a waypoint is walked on, increases i and decreases progress conveniently. */
  def get_position: ()Waypoint
  /* Returns the current actual position of the bunny (taking progress into account) */
}
