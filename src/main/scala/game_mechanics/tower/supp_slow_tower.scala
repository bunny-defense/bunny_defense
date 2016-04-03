
package game_mechanics.tower

import java.awt.Graphics2D
import java.awt.AlphaComposite
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import game_mechanics.bunny.Bunny
import gui.{Colors,MapPanel,SnowAnimation}
import runtime.Controller

object SuppSlowTower extends TowerType
{
    override val name = "Support slowing tower"
    override val desc = "Slows down nearby enemies"
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
    override val unlock_wave    = 25
    override def attack_from(tower: Tower): () => Boolean = {
        def new_snow_anim() : Unit = {
            val snow_anim = new SnowAnimation( tower.pos, tower.range )
            snow_anim and_then new_snow_anim
            Controller += snow_anim
        }
        new_snow_anim()
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
}

