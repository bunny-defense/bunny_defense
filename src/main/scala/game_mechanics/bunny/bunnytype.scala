
package game_mechanics.bunny

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import util.Random

import game_mechanics.Player
import runtime.Controller
import game_mechanics.path._
import gui.GoldAnimation

import Math._

trait BunnyType
{
    /** The trait that defines a bunny type.
     *  Every bunny has a type, from which it inherits the attributes,
     *  and the attacking specificities
     */
    val bunny_graphic =
        ImageIO.read(
            new File(getClass().getResource("/mobs/bunny_alt1.png").getPath()))
    val initial_hp    = 10.0  /* Initial amount of HP */
    val base_shield   = 1.0
    var shield        = 1.0   /* Damage dampening */
    val base_speed    = 2.0
    var speed         = 2.0   /* Speed of the bunny in tiles per second */
    /* The following takes three values : init_val, final_val and inflex_point,
    THAT MUST VERIFY init_val >= final_val, and returns the arctangent function
    decreasing to final_val from init_val with an inflexion point at inflex_point. */
    def atan_variation (
        init_val : Int,
        final_val : Int,
        inflex_point : Int) : (Int => Int) = {
        def res (nwave : Int) : Int = {
            //println(nwave.toString)
            (1.25*(1.57 + atan(4*(inflex_point - nwave)/inflex_point))*
            ((init_val - final_val)/(3.1416)) + final_val).toInt}
            /* The factor 1.25 in the beginning does make the function go above
            its maximum value, but before 0, and is here to make sure the
            function starts at (approximately) its max value */
        return res
    }

    /* Amount of gold earned when killed */
    def reward : (Int => Int)  = atan_variation(5, 1, 10)

    var damage        = 1     /* Damage done to the player when core reached */

    /* Whenever there is a special effect on death */
    def on_death(bunny: Bunny): Unit = {}

    /* The update of a bunny. */
	def update(bunny: Bunny, dt: Double): Unit = {
        if ( !bunny.alive ) {
            on_death(bunny)
            Controller += new GoldAnimation(
                bunny.bunny_type.reward(Controller.wave_counter),
                bunny.pos.clone()
            )
            Player.add_gold( bunny.bunny_type.reward( Controller.wave_counter ))
            Controller -= bunny
            Player.killcount += 1
        }
        bunny.move(dt)
        if ( bunny.path.reached ) {
            Player.remove_hp( bunny.bunny_type.damage )
            Controller -= bunny
        }
    }
}
