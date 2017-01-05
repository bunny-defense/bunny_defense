
package game_mechanics.bunny

import java.io.File
import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import Math._

import game_mechanics.path._
import game_mechanics.{Player, JPS}
import gui.MapPanel
import gui.animations.GoldAnimation
import runtime.TowerDefense
import runtime.GameState
import util.Random

/**
 Bunny superclass from which every ennemy is derived.
 @param _owner The player who owns the bunny
 @param _path The path the bunny has to take
 @param gamestate The state of the game
 */
abstract class Bunny(
    _owner: Player,
    _path: Progress,
    gamestate: GameState)
{
    val id : Int
    val owner : Player  = _owner
    var hp              = 10.0
    var initial_hp      = 10.0
    val law             = new Random()
    var path            = _path
    var pos : Waypoint  = new Waypoint(0,0)
    val base_shield     = 1.0
    var shield          = 1.0
    var base_speed      = 2.0
    var speed           = 2.0
    val spread          = (Waypoint.random() * 2 - new Waypoint(1,1)) / MapPanel.cellsize * 2
    val bunny_graphic =
        ImageIO.read(
            new File(getClass().getResource("/mobs/bunny_chevaliey.png").getPath()))
    val effect_range    = 9

    def allied_effect(bunny: Bunny): Unit = {}
    val damage          = 1
    var last_damager : Option[Player] = None


    /** Computes the reward value according to the wave counter
     * @param init_val     : initial val of the atan variation
     * @param final_val    : final val of the atan variation
     * @param inflex_point : inflexion point of the atan
     * The following takes three values : init_val, final_val and inflex_point,
     */
    def atan_variation (
        init_val : Int,
        final_val : Int,
        inflex_point : Int) : (Int => Int) = {
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

    def remove_hp(dmg: Double, player: Player): Unit = {
        this.hp -= dmg * (1.0 - this.shield/10.0)
        last_damager = Some(player)
    }

    def alive() : Boolean = {
        hp > 0
    }

    /* Moves the bunny along the path */
    protected def move(dt: Double): Unit = {
        path.move( dt * this.speed )
        pos = path.get_position + spread
    }

	def update(dt: Double): Unit = {
        if ( !this.alive ) {
            this.on_death()
            gamestate.bunny_death_render_strategy(this)
            last_damager.get.add_gold(reward(gamestate.wave_counter))
            gamestate -= this
            last_damager.get.killcount += 1
        }
        this.move(dt)
        if ( this.path.reached ) {
            gamestate -= this
            gamestate.bunny_reach_goal_strategy(this)
        }
    }

    // =================
    // ++++ GETTERS ++++
    // =================

    def graphic(): BufferedImage = {
        return this.bunny_graphic
    }

}


