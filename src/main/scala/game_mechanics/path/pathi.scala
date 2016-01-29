trait PathTrait {
  val wps: ListBuffer[Waypoint]
  /* List of waypoints on the path */
  def Path( p: Path ): Unit
  /* Copy-constructor for the path */
  def add( wp: Waypoint ): Unit
  /* Another constructor for the path : adds wp to the list of waypoints */
  def at( wp: Waypoint ): Waypoint
  /* The i-th waypoint on the path */
  def length(): Int
  /* The length of the path */
}
