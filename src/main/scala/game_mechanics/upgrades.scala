package game_mechanics
import game_mechanics._
import game_mechanics.tower._
import Math._

trait UpgradeTree
{
    /** A trait that defines the type and methods of an upgrade tree */
    val name = "Upgrade name"
    val description = "Upgrade description"
    val cost = 100
    def effect (tower : Tower) =
    {}
    val children : Option[UpgradeTree] = None /* TO DO */
}

object BaseTowerUpgrades extends UpgradeTree
{
    override val name = "Heavy carrots"
    override val description = "Throw heavier carrots, dealing more damage"
    override def effect(tower : Tower)
    {
        tower.base_damage +=  5
        tower.upgrades = this.children
    }
}

object QuickTowerUpgrades extends UpgradeTree
{
    override val name = "Ballistic carrots"
    override val description = "Long-range targetting system allows the tower to throw carrots further"
    override def effect(tower : Tower)
    {
        tower.base_range = tower.base_range + 2
        tower.upgrades = this.children
    }
}
