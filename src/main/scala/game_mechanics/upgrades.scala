package game_mechanics
import game_mechanics._
import game_mechanics.tower._
import Math._

trait UpgradeTree
{
    val name = "Upgrade name"
    val description = "Upgrade description"
    val cost = 100
    def effect (tower : Tower) =
    {}
    val children = 0 /* TO DO */
}

object BaseTowerUpgrades extends UpgradeTree
{
    override val name = "Heavy carrots"
    override val description = "Throw heavier carrots, dealing more damage"
    override def effect(tower : Tower)
    {
        tower.damage = tower.damage + 5 /* Just to actually see if the upgrade works */
    }
}

object QuickTowerUpgrades extends UpgradeTree
{
    override val name = "Ballistic carrots"
    override val description = "Long-range targetting system allows the tower to throw carrots further"
    override def effect(tower : Tower)
    {
        tower.range = tower.range + 2
    }
}

