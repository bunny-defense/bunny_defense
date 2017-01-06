
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
 @param owner The player who owns the bunny
 @param path The path the bunny has to take
 @param gamestate The state of the game
 @param base_hp The base health points of the bunny
 @param health_modifier Multiplied to _initial_hp
 */
abstract class Bunny(
    val owner: Player,
    val path: Progress,
    val gamestate: GameState,
    val base_hp: Double = 10.0,
    val health_modifier: Double = 1.0)
{
    /** Unique id associated with that bunny */
    val id : Int
    /** The initial health points of the bunny */
    private val _initial_hp = base_hp * health_modifier
    /** The current health points of the bunny */
    private var _hp     = _initial_hp

    val law             = new Random()
    /** The start position of the bunny */
    var pos : Waypoint  = new Waypoint(0,0)
    /** The base value for the shield */
    val base_shield     = 1.0
    /** The current value of the shield */
    var shield          = 1.0
    /** The base value of the bunny speed */
    var base_speed      = 2.0
    /** The current value of the bunny speed */
    var speed           = 2.0
    val spread          = (Waypoint.random() * 2 - new Waypoint(1,1)) / MapPanel.cellsize * 2
    /** The image representing the bunny */
    val bunny_graphic =
        ImageIO.read(
            new File(getClass().getResource("/mobs/bunny_chevaliey.png").getPath()))
    val effect_range    = 9

    def allied_effect(bunny: Bunny): Unit = {}
    /** The damage caused to the player when reaching the end */
    val damage          = 1
    /** The last player to have caused damage to the bunny */
    var last_damager : Option[Player] = None


    /** Computes the reward value according to the wave counter
     * @param init_val     initial val of the atan variation
     * @param final_val    final val of the atan variation
     * @param inflex_point inflexion point of the atan
     * @return A function outputing the reward from the wave count
     * The following takes three values : init_val, final_val and inflex_point,
     */
    protected def atan_variation (
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

    /** Computes the reward offered by killing the bunny according to the
     wave counter
     @param wave_count The current wave count
     @return The rewared offered by the bunny
     */
    def reward: (Int => Int) = atan_variation(5, 1, 10)

    /** This function is triggered when the bunny dies */
    protected def on_death(): Unit = {}

    /** This function applies damage to the bunny.
     @param dmg The amount of damaged caused to the bunny
     @param player The player dealing damage to the bunny
     */
    def remove_hp(dmg: Double, player: Player): Unit = {
        this._hp -= dmg * (1.0 - this.shield/10.0)
        last_damager = Some(player)
    }

    /**
     @return true if the bunny is alive, false otherwise
     */
    def alive() : Boolean = {
        this._hp > 0.0
    }

    /** Moves the bunny along the path
     @param dt The delta of time that passed since the last update
     */
    protected def move(dt: Double): Unit = {
        path.move( dt * this.speed )
        pos = path.get_position + spread
    }

    /** Updates the bunny
     @param dt The delta of time that passed since the last update
     */
	def update(dt: Double): Unit = {
        if ( !this.alive ) {
            this.on_death()
            gamestate.bunny_death_render_strategy(this)
            val damager = last_damager.get // If this crashes, it is a bug
            damager.add_gold(reward(gamestate.wave_counter))
            damager.killcount += 1
            gamestate -= this
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

    def initial_hp(): Double = {
        return this._initial_hp
    }

    def hp(): Double = {
        return this._hp
    }

}


