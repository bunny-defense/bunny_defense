
package game_mechanics.tower

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import game_mechanics._
import game_mechanics.bunny._
import runtime.TowerDefense

object OtterSpawnerTower extends SpawnerTower()
{
    override val name = "Otter barn"
    override val desc = "Otters don't live in barns, duh"
    bunnies_spawning  = List(BunnyFactory.OTTER_)
    base_range                  = 4
    range                       = 4
    override val fire_speed    = 15.0
    override val fire_cooldown = 10.0
    base_damage                 = 9
    damage                      = 9
    override val price          = 2000
    sell_cost                   = 1000
    override val unlock_wave    = 1
    upgrades                    = Some(Spawner_SpawnRateUpgrade)
    override def serialize() : Int = TowerType.OTTER_SPAWNER
}
