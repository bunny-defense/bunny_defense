
package game_mechanics.tower

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import game_mechanics._
import game_mechanics.bunny._
import runtime.TowerDefense

object HareSpawnerTower extends SpawnerTower()
{
    override val name = "Hare barn"
    override val desc = "Creates quick hares"
    bunnies_spawning  = List(BunnyFactory.HARE_, BunnyFactory.HARE_)
    base_range                 = 4
    range                      = 4
    override val throw_speed   = 15.0
    base_damage                = 9
    damage                     = 9
    override val price         = 1
    sell_cost                  = 75
    override val unlock_wave   = 1
    upgrades                   = Some(Spawner_SpawnRateUpgrade)
    override def serialize() : Int = TowerType.HARE_SPAWNER
}
