
package game_mechanics.tower

import collection.mutable.ListBuffer

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import runtime.{Controller,Spawner}
import game_mechanics.{Projectile,Bunny}
import game_mechanics.path._


trait TowerType
{
    val tower_graphic  =
        ImageIO.read(
            new File(
                getClass().getResource("/towers/base_tower.png").getPath()))
    val throw_graphic  =
        ImageIO.read(
            new File(
                getClass().getResource("/projectiles/carrot1.png").getPath()))
    val size           = 1      /* Size in tiles */
    val base_damage    = 5
    var damage         = 5      /* Damage dealt to bunnies */
    val base_range     = 5
    var range          = 5      /* Range in tiles */
    val spread         = 0.0    /* Amount of bullet spread */
    val throw_speed    = 10.0   /* Speed of the shot projectile */
    val throw_cooldown = 1.0    /* Cooldown time in seconds */
    val buy_cost       = 50     /* Gold needed to buy one */
    val sell_cost      = 25     /* Gold earned when sold */
    def allied_effect(tower : Tower) : Unit = { } /* Applies the tower's effect on allied towers */
    def enemy_effect(bunny : Bunny) : Unit = { } /* Applies the tower's effect on enemy bunnies */

    var amount         = 0

    def attack_from(tower : Tower): () => Boolean = { () => true }
}

