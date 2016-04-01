
package game_mechanics

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import runtime.Controller
import game_mechanics.path._
import gui.GoldAnimation

import Math._

trait BunnyType
{
    val bunny_graphic =
        ImageIO.read(
            new File(getClass().getResource("/mobs/bunny_alt1.png").getPath()))
    val initial_hp    = 10.0  /* Initial amount of HP */
    val base_shield   = 1.0
    var shield        = 1.0   /* Damage dampening */
    val base_speed    = 2.0
    var speed         = 2.0   /* Speed of the bunny in tiles per second */
    /* The following takes three values : init_val, final_val and inflex_point, THAT MUST VERIFY init_val >= final_val, and returns the arctangent function decreasing to final_val from init_val with an inflexion point at inflex_point. */
    def atan_variation (init_val : Int, final_val : Int, inflex_point : Int) : (Int => Int) = {
        def res (nwave : Int) : Int = {
            //println(nwave.toString)
                (1.25*(1.57 + atan(4*(inflex_point - nwave)/inflex_point))*((init_val - final_val)/(3.1416)) + final_val).toInt}
            /* The factor 1.25 in the beginning does make the function go above its maximum value, but before 0, and is here to make sure the function starts at (approximately) its max value */
        return res
    }
    def reward : (Int => Int)  = atan_variation(5, 1, 10)
    /* Amount of gold earned when killed */
    var damage        = 1     /* Damage done to the player when core reached */
}

object NormalBunny extends BunnyType

/* Large and tough but slow bunny */
object HeavyBunny extends BunnyType
{
    override val initial_hp  = 20.0
    override val base_shield = 1.5
    shield                   = 1.5
    override val base_speed  = 1.0
    speed                    = 1.0
    override def reward      = atan_variation(10,2,10)
}

/* A badass bunny, really strong, will become the "default" mob in late game */
object BadassBunny extends BunnyType
{
  override val bunny_graphic =
    ImageIO.read(
      new File(getClass().getResource("/mobs/badassbunny.png").getPath()))
  override val initial_hp  = 30.0
  override val base_shield = 2.0
  shield                   = 2.0
  override val base_speed  = 1.5
  speed                    = 1.5
  override def reward      = atan_variation(15,3,15)
}

/* Fast "Bunny" */
object Hare extends BunnyType
{
    override val bunny_graphic =
        ImageIO.read(new File(
            getClass().getResource("/mobs/hare_alt1.png").getPath()))
    override val initial_hp  = 5.0
    override val base_shield = 0.0
    shield                   = 0.0
    override val base_speed  = 4.0
    speed                    = 4.0
}

/* A boss! */
object Otter extends BunnyType
{
    override val bunny_graphic =
        ImageIO.read(
            new File(getClass().getResource("/mobs/otter.png").getPath()))
    override val initial_hp  = 1000.0
    override val base_shield = 1.5
    shield                   = 1.5
    override val base_speed  = 1.0
    speed                    = 1.0
    damage     = 5
    override def reward     = atan_variation(100,15,25)
}

/* Rare golden bunny worth a lot of money */
object GoldenBunny extends BunnyType
{
    override val bunny_graphic =
        ImageIO.read(new File(
            getClass().getResource("/mobs/goldenbunny_alt1.png").getPath()))
    override val initial_hp    = 20.0
    override val base_speed    = 8.0
    speed                      = 8.0
    override def reward        = atan_variation(500,500,1) /* Constant at 500 */
}

/* Bunny superclass from which every ennemy is derived. */
class Bunny(bunny_type: BunnyType,path0: Path) {
    var hp              = bunny_type.initial_hp
    var pos : Waypoint  = path0.waypoints(0)
    var path            = new Progress(path0)
    val base_shield     = bunny_type.base_shield
    var shield          = bunny_type.shield
    val base_speed      = bunny_type.base_speed
    var speed           = bunny_type.speed

    def reward          = bunny_type.reward
    /* Prototype design pattern */
    def copy(): Bunny = {
        return new Bunny( bunny_type, path.path )
    }

    def remove_hp(dmg: Double): Unit = {
        this.hp -= dmg * (1.0 - this.shield/10.0)
    }

    def alive() : Boolean = {
        hp > 0
    }

    /* Moves the bunny along the path */
    def move(dt: Double): Unit = {
        path.move( dt * this.speed )
        pos = path.get_position
    }

    def update(dt: Double): Unit = {
        if( !alive )
        {
            Controller += new GoldAnimation( bunny_type.reward(Controller.wave_counter), pos.clone() )
            Player.add_gold( this.reward(Controller.wave_counter) )
            Controller -= this
            Player.killcount += 1
        }
        move(dt)
        if( path.reached )
        {
            Player.remove_hp( bunny_type.damage )
            Controller -= this
        }
    }

    def initial_hp(): Double = {
        return bunny_type.initial_hp
    }

    def graphic(): BufferedImage = {
        return bunny_type.bunny_graphic
    }
}


