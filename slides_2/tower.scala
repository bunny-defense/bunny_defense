trait TowerType
{
  val tower_graphic  =
    ImageIO.read(
    new File(getClass().
    getResource("/towers/base_tower.png").getPath()))
  val size           = 1
  val damage         = 5
  val range          = 5
  val aoe_radius     = 0
  val throw_speed    = 10.0
  val throw_cooldown = 1.0
  val buy_cost       = 50
  val sell_cost      = 25
}

