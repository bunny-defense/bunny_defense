package game_mechanics
import game_mechanics._
import game_mechanics.tower._
import game_mechanics.bunny._
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

object DamageUpgrade extends UpgradeTree
{
    override val name = "Heavy carrots"
    override val description = "Damage +5"
    override def effect(tower : Tower)
    {
        tower.base_damage +=  5
        tower.upgrades = this.children
    }
}

object RangeUpgrade extends UpgradeTree
{
    override val name = "Ballistic carrots"
    override val description = "Range + 2"
    override def effect(tower : Tower)
    {
        tower.base_range = tower.base_range + 2
        tower.upgrades = this.children
    }
}

object FireRateUpgrade extends UpgradeTree
{
    override val name = "Fully automatic carrot rifle"
    override val description = "Fire rate * 1.5"
    override def effect(tower : Tower)
    {
        tower.base_throw_cooldown = tower.base_throw_cooldown * 2/3
        tower.upgrades = this.children
    }
}

object Spawner_SpawnRateUpgrade extends UpgradeTree
{
    override val name = "Bunnies mate quickly"
    override val description = "Spawn rate * 1.5"
    override def effect(tower : Tower)
    {
        tower.base_throw_cooldown = tower.base_throw_cooldown * 2/3
        tower.upgrades = this.children
    }
}

object Spawner_BunniesSpeedUpgrade extends UpgradeTree
{
    override val name = "Genetically engineered bunnies"
    override val description = "Spawned bunnies speed * 1.5"
    override def effect(tower : Tower)
    {
        /* TO DO */
        tower.upgrades = this.children
    }
}

object Spawner_AddHeavyBunnyUpgrade extends UpgradeTree
{
    override val name = "Heavytisation"
    override val description = "Regularly creates a heavy bunny"
    override def effect(tower : Tower)
    {
        tower.bunnies_spawning = tower.bunnies_spawning ::: List(BunnyFactory.HEAVY_BUNNY)
        tower.upgrades = this.children
    }
}
