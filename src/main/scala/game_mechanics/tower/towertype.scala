
package game_mechanics.tower

import collection.mutable.ListBuffer

import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import runtime.TowerDefense
import runtime.GameState
import game_mechanics.Projectile
import game_mechanics.bunny.Bunny
import game_mechanics.path._
import game_mechanics._

object TowerType
{
    val BASE_TOWER      = 1
    val QUICK_TOWER     = 2
    val HEAVY_TOWER     = 3
    val ROBERTO         = 4
    val SCARECROW       = 5
    val SUPP_SLOW_TOWER = 6
    val SUPP_BUFF_TOWER = 7
    val SPLASH_TOWER    = 8
    val BASE_SPAWNER    = 9
    val OTTER_SPAWNER   = 10
    val HARE_SPAWNER    = 11
    val HEAVY_SPAWNER   = 12
    val QUICK_SPAWNER   = 13
    val SUPPORT_SPAWNER = 14
    val WALL            = 15
    def deserialize(value: Int) : TowerType = {
        value match {
            case BASE_TOWER =>
                BaseTower
            case QUICK_TOWER =>
                QuickTower
            case HEAVY_TOWER =>
                HeavyTower
            case ROBERTO =>
                Roberto
            case SCARECROW =>
                ScarecrowTower
            case SUPP_SLOW_TOWER =>
                SuppSlowTower
            case SUPP_BUFF_TOWER =>
                SuppBuffTower
            case SPLASH_TOWER =>
                SplashTower
            case BASE_SPAWNER =>
                BaseSpawnerTower
            case OTTER_SPAWNER =>
                OtterSpawnerTower
            case HARE_SPAWNER =>
                HareSpawnerTower
            case HEAVY_SPAWNER =>
                HeavySpawnerTower
            case QUICK_SPAWNER =>
                QuickSpawnerTower
            case SUPPORT_SPAWNER =>
                SupportSpawnerTower
            case WALL =>
                Wall
            case _ =>
                throw new Exception("Not a tower type")
        }
    }
}
trait TowerType
{
    /** A trait that defines the type of a tower. Every tower type inherits this
     *  trait, which gives the default attributes, and specific methods
     */
    val name = "Tower"
    val desc = "Tower tower tower"
    var upgrades : Option[UpgradeTree] = None
    val tower_graphic  =
        ImageIO.read(
            new File(
                getClass().getResource("/towers/base_tower.png").getPath()))
    val throw_graphic  =
        ImageIO.read(
            new File(
                getClass().getResource("/projectiles/carrot1.png").getPath()))
    val size           = 1      /* Size in tiles */
    var bunnies_spawning = List[Int]()
    var base_damage    = 5
    var damage         = 5      /* Damage dealt to bunnies */
    var base_range     = 5
    var range          = 5      /* Range in tiles */
    val spread         = 0.0    /* Amount of bullet spread */
    val fire_speed     = 10.0   /* Speed of the shot projectile */
    val fire_cooldown  = 1.0    /* Cooldown time in seconds */
    val price          = 50     /* Gold needed to buy one */
    var sell_cost      = 25     /* Gold earned when sold */
    val unlock_wave    = 1      /* First round of apparition */
    def allied_effect(tower : Tower) : Unit = { } /* Applies the tower's effect on allied towers */
    def enemy_effect(bunny : Bunny) : Unit = { } /* Applies the tower's effect on enemy bunnies */

    var amount         = 0

    def attack_from(tower : Tower, gamestate: GameState): () => Boolean = { () => true }

    def draw_effect(g: Graphics2D): Unit = {}
    def serialize(): Int
}

