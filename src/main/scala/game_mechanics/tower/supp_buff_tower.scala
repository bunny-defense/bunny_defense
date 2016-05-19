
package game_mechanics.tower

import java.awt.Graphics2D
import java.awt.AlphaComposite
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import runtime.TowerDefense
import runtime.GameState
import gui.{Colors,MapPanel}
import gui.animations.BuffAnimation
import game_mechanics.bunny.Bunny

object SuppBuffTower extends TowerType
{
    override val name = "Support buff tower"
    override val desc = "Increases nearby towers' damage"
    override val tower_graphic  =
        ImageIO.read(
            new File(
                getClass().getResource("/towers/radar.png").getPath()))
    override val throw_graphic  =
        ImageIO.read(
            new File(
                getClass().getResource("/blank.png").getPath()))
    base_range                  = 5
    range                       = 5
    base_damage                 = 0
    damage                      = 0
    override val price       = 6000
    sell_cost                   = 4800
    override val unlock_wave    = 25
    override def attack_from(
        tower: Tower, gamestate: GameState): () => Boolean = {
            gamestate.supp_buff_tower_animation_strategy(tower)
            () => true
    }
    override def allied_effect(tower : Tower) {
        /* The argument is the tower ON WHICH the effect is cast */
        tower.damage *= 2
    }
    override def draw_effect(g: Graphics2D): Unit = {
        val alpha = (Math.sin( System.currentTimeMillis.toDouble / 1000 ) + 1) / 2 * 0.1
        g.setComposite(
            AlphaComposite.getInstance( AlphaComposite.SRC_OVER, alpha.toFloat ) )
        g.setColor( Colors.orange )
        val maprange = range * MapPanel.cellsize
        g.fillOval( -maprange, -maprange, maprange * 2, maprange * 2 )
    }
    override def serialize() : Int = TowerType.SUPP_BUFF_TOWER
}

