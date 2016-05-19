
package game_mechanics.tower

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import game_mechanics._
import game_mechanics.bunny._
import runtime.TowerDefense

object BaseSpawnerTower extends SpawnerTower()
{
    override val name = "Bunny barn"
    override val desc = "Creates base bunnies"
    bunnies_spawning  = List(BunnyFactory.NORMAL_BUNNY, BunnyFactory.NORMAL_BUNNY)
    base_range                 = 4
    range                      = 4
    override val throw_speed   = 15.0
    base_damage                = 9
    damage                     = 9
    override val price         = 1
    sell_cost                  = 75
    override val unlock_wave   = 1
    upgrades                   = Some(Spawner_AddHeavyBunnyUpgrade)
    override def serialize() : Int = TowerType.BASE_SPAWNER
}
