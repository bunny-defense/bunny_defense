package game_mechanics.upgrades
import game_mechanics._
import game_mechanics.tower._
import Math._

trait SingleUpgrade
{
    /** A trait that defines the type and methods of an upgrade tree */
    val name = "Upgrade name"
    val description = "Upgrade description"
    val cost = 100
    def effect (tower : Tower) =
    {}
    val children : List[SingleUpgrade] = List()
}

object BaseTowerUpgrades extends SingleUpgrade
{
    override val name = "Heavy carrots"
    override val description = "Damage +5"
    override def effect(tower : Tower)
    {
        tower.base_damage +=  5
    }
}

object QuickTowerUpgrades extends SingleUpgrade
{
    override val name = "Ballistic carrots"
    override val description = "Range + 2"
    override def effect(tower : Tower)
    {
        tower.base_range = tower.base_range + 2
    }
    override val children = List(BaseTowerUpgrades)
}
