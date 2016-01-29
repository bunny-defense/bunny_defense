trait MapcellTrait {
  var walkable: Bool
  /* Can the cell be walked on? */
  def UpgradeCell( new_value: Boolean ): Unit
  /* Updates walkable to the value new_value */
}
