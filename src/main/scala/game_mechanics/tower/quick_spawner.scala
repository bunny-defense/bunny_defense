
package game_mechanics.tower

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import game_mechanics._
import game_mechanics.bunny._
import runtime.TowerDefense

object QuickSpawnerTower extends SpawnerTower()
{
    override val name = "Ninja barn"
    override val desc = "Creates sneaky bunnies"
    bunnies_spawning  = List(BunnyFactory.FLYING_SQUIRREL, BunnyFactory.FLYING_SQUIRREL,BunnyFactory.SPECOP_BUNNY)
    base_range                 = 4
    range                      = 4
    override val fire_speed   = 15.0
    base_damage                = 9
    damage                     = 9
    override val price         = 200
    sell_cost                  = 100
    override val unlock_wave   = 1
    upgrades                   = Some(Spawner_BunniesSpeedUpgrade)
    override def serialize() : Int = TowerType.QUICK_SPAWNER
}
