
package game_mechanics.tower

import java.awt.Graphics2D
import java.awt.AlphaComposite
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import runtime.TowerDefense
import runtime.GameState
import game_mechanics.bunny.Bunny
import gui.{Colors,MapPanel}

object SuppSlowTower extends TowerType
{
    override val name = "Support slowing tower"
    override val desc = "Slows down nearby enemies"
    override val tower_graphic  =
        ImageIO.read(
            new File(
                getClass().getResource("/towers/frozenmachine.png").getPath()))
    override val throw_graphic  =
        ImageIO.read(
            new File(
                getClass().getResource("/blank.png").getPath()))
    base_range                  = 5
    range                       = 5
    base_damage                 = 0
    damage                      = 0
    override val price          = 5000
    sell_cost                   = 4000
    override val unlock_wave    = 25
    override def attack_from(tower: Tower, gamestate: GameState): () => Boolean = {
        gamestate.supp_slow_tower_animation_strategy(tower)
        () => true
    }
    override def enemy_effect(bunny : Bunny) {
        /* The argument is the bunny ON WHICH the effect is cast */
        bunny.speed = bunny.speed / 2
    }
    override def draw_effect(g: Graphics2D): Unit = {
        val alpha = (Math.sin( System.currentTimeMillis.toDouble / 1000 ) + 1) / 2 * 0.1
        g.setComposite(
            AlphaComposite.getInstance( AlphaComposite.SRC_OVER, alpha.toFloat ) )
        g.setColor( Colors.cyan )
        val maprange = range * MapPanel.cellsize
        g.fillOval( -maprange, -maprange, maprange * 2, maprange * 2 )
    }
    override def serialize() : Int = TowerType.SUPP_SLOW_TOWER
}

