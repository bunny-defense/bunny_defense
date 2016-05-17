
package game_mechanics.tower

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import game_mechanics._
import game_mechanics.bunny._
import runtime.TowerDefense

object HeavySpawnerTower extends SpawnerTower()
{
    override val name = "Heavy barn"
    override val desc = "Creates slower, but stronger bunnies"
    bunnies_spawning  = List(BunnyFactory.HEAVY_BUNNY, BunnyFactory.HEAVY_BUNNY, BunnyFactory.BADASS_BUNNY)
    base_range                  = 4
    range                       = 4
    override val throw_speed    = 15.0
    base_damage                 = 9
    damage                      = 9
    override val throw_cooldown = 2.0
    override val price          = 1
    sell_cost                   = 75
    override val unlock_wave    = 1
    upgrades                    = Some(Spawner_HealthUpgrade)
}
