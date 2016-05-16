
package game_mechanics.bunny

import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import Math._

import game_mechanics.path._
import game_mechanics.{Player, JPS, Purchasable}
import gui.MapPanel
import gui.animations.GoldAnimation
import runtime.TowerDefense
import util.Random
import strategy._


trait Bunny extends Purchasable {
    /**
     * Bunny superclass from which every ennemy is derived.
     */
    val player          = 0
    val id : Int
    var hp              = 10.0
    val initial_hp      = 10.0
    val law             = new Random()
    var path :  Progress
    var bunnyend        = path.last.toInt
    var pos : Waypoint  = path.path.head
    val base_shield     = 1.0
    var shield          = 1.0
    val base_speed      = 2.0
    var speed           = 2.0
    val spread          = (Waypoint.random() * 2 - new Waypoint(1,1)) / MapPanel.cellsize * 2
    val bunny_graphic =
        ImageIO.read(
            new File(getClass().getResource("/mobs/bunny_chevaliey.png").getPath()))
    val effect_range    = 9

    def allied_effect(bunny: Bunny): Unit = {}
    val damage          = 1
    val price           = 10


    def atan_variation (
        init_val : Int,
        final_val : Int,
        inflex_point : Int) : (Int => Int) = {
    /** Computes the reward value according to the wave counter
     * @param init_val     : initial val of the atan variation
     * @param final_val    : final val of the atan variation
     * @param inflex_point : inflexion point of the atan
     * The following takes three values : init_val, final_val and inflex_point,
     */
        def res (nwave : Int) : Int = {
            (1.25*(1.57 + atan(4*(inflex_point - nwave)/inflex_point))*
            ((init_val - final_val)/(3.1416)) + final_val).toInt
        }
            /* The factor 1.25 in the beginning does make the function go above
            its maximum value, but before 0, and is here to make sure the
            function starts at (approximately) its max value */
        return res
    }

    def reward: (Int => Int) = atan_variation( 5, 1, 10)

    def on_death(): Unit = {}

    def remove_hp(dmg: Double): Unit = {
        this.hp -= dmg * (1.0 - this.shield/10.0)
    }

    def alive() : Boolean = {
        hp > 0
    }

    /* Moves the bunny along the path */
    def move(dt: Double): Unit = {
        path.move( dt * this.speed )
        pos = path.get_position + spread
    }

	def update(dt: Double): Unit = {
        if ( !this.alive ) {
            TowerDefense.gamestate.strategy.updatestrategy.on_death(this)
        }
        this.move(dt)
        if ( this.path.reached ) {
            TowerDefense.gamestate.strategy.updatestrategy.lost_hp(this)
        }
    }

    // =================
    // ++++ GETTERS ++++
    // =================

    def graphic(): BufferedImage = {
        return this.bunny_graphic
    }

}


