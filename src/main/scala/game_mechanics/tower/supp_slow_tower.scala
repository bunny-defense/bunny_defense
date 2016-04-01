
package game_mechanics.tower

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import game_mechanics.Bunny

object SuppSlowTower extends TowerType
{
    override val tower_graphic  =
        ImageIO.read(
            new File(
                getClass().getResource("/towers/roberto.png").getPath()))
    override val throw_graphic  =
        ImageIO.read(
            new File(
                getClass().getResource("/blank.png").getPath()))
    override val base_range     = 5
    range                       = 5
    override val base_damage    = 0
    damage                      = 0
    override val buy_cost       = 50
    override val sell_cost      = 35
    override def enemy_effect(bunny : Bunny) {
        /* The argument is the bunny ON WHICH the effect is cast */
        bunny.speed = bunny.speed / 2
    }
    override def fire_from(tower: Tower)(bunny: Bunny) {}
}

