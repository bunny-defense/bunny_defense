
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

trait TowerType extends Purchasable
{
    /** A trait that defines the type of a tower. Every tower type inherits this
     *  trait, which gives the default attributes, and specific methods
     */
    val name = "Tower"
    val desc = "Tower tower tower"
    var upgrades : Option[UpgradeTree] = Some(BaseTowerUpgrades)
    val tower_graphic  =
        ImageIO.read(
            new File(
                getClass().getResource("/towers/base_tower.png").getPath()))
    val throw_graphic  =
        ImageIO.read(
            new File(
                getClass().getResource("/projectiles/carrot1.png").getPath()))
    val size           = 1      /* Size in tiles */
    var base_damage    = 5
    var damage         = 5      /* Damage dealt to bunnies */
    var base_range     = 5
    var range          = 5      /* Range in tiles */
    val spread         = 0.0    /* Amount of bullet spread */
    val throw_speed    = 10.0   /* Speed of the shot projectile */
    val throw_cooldown = 1.0    /* Cooldown time in seconds */
    val price          = 50     /* Gold needed to buy one */
    var sell_cost      = 25     /* Gold earned when sold */
    val unlock_wave    = 1      /* First round of apparition */
    def allied_effect(tower : Tower) : Unit = { } /* Applies the tower's effect on allied towers */
    def enemy_effect(bunny : Bunny) : Unit = { } /* Applies the tower's effect on enemy bunnies */

    var amount         = 0

    def attack_from(tower : Tower, gamestate: GameState): () => Boolean = { () => true }

    def draw_effect(g: Graphics2D): Unit = {}
}

