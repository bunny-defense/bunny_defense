
package game_mechanics.tower

import collection.mutable.ListBuffer

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import game_mechanics._
import game_mechanics.path._
import runtime.{TowerDefense,GameState}
import game_mechanics.bunny._
import game_mechanics.JPS
import util.Random

class SpawnerTower extends TowerType
{
    /**
     * The class that defines the methods of all spawners
      */
    val law = new Random()
    val target : Option[Player] = None
    override val tower_graphic =
        ImageIO.read(
            new File(
                getClass().getResource("/towers/barn.png").getPath()))
    /* The following are stats modifiers that apply to spawned bunnies */
    override def attack_from(
        tower: Tower,
        gamestate: GameState): () => Boolean = {
            val available = gamestate.players.filter(_.id != tower.owner.id)
            def choose_target() : Player = {
                return available.apply(law.nextInt(available.length))
            }
            def get_right_type(): Boolean = {
                var new_bunny =
                    target match {
                        case Some(target1) => {
                            val path = new JPS( tower.pos, target1.base, gamestate)
                                .run() match {
                                    case Some(p) => p
                                    case None => throw new Exception()
                                }
                                println(target.toString)
                                BunnyFactory.create(
                                    tower.bunnies_spawning.head,
                                    tower.owner,
                                    new Progress(path),
                                    gamestate
                                )
                        }
                        case None => {
                            val target2 = choose_target()
                            val path = new JPS( tower.pos, target2.base, gamestate)
                                    .run() match {
                                        case Some(p) => p
                                        case None => throw new Exception()
                                    }
                                    println(target2.toString + "    " + target2.base.toString)
                                    println(tower.owner.base.toString)
                                    BunnyFactory.create(
                                        tower.bunnies_spawning.head,
                                        tower.owner,
                                        new Progress(path),
                                        gamestate
                                    )
                        }
                    }
                    new_bunny.base_speed = new_bunny.base_speed * tower.speed_modifier
                    new_bunny.hp = new_bunny.initial_hp * tower.health_modifier
                    new_bunny.initial_hp = new_bunny.initial_hp * tower.health_modifier
                    gamestate += new_bunny
                    tower.bunnies_spawning = tower.bunnies_spawning.tail ::: List(tower.bunnies_spawning.head)
                    return true
            }
            return get_right_type
    }
    override def serialize() : Int = -1
}

