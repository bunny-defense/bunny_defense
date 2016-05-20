
package game_mechanics.tower

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import collection.mutable.ListBuffer

import runtime.TowerDefense
import runtime.GameState
import game_mechanics.path.Waypoint
import game_mechanics.bunny.Bunny
import game_mechanics.Player
import gui.animations.SpreadAnimation



/* The class of a utilitary purchasable item */
class Utilitary(_owner: Player, origin_pos: Waypoint)  extends TowerType{
    override val name  = "Mine"
    override val desc  = "A little mine"
    override val tower_graphic =
        ImageIO.read(
            new File(
                getClass().getResource("towers/trap.gif").getPath()))
    override val throw_graphic =
        ImageIO.read(
            new File(
                getClass().getResource("projectiles/potato-image.png").getPath()))
    damage             = 5
    base_damage        = 5
    var pos            = origin_pos
    val hitradius      = 2
    range              = 0
    override val price = 15
    val owner          = _owner

    override def attack_from(tower : Tower, gamestate: GameState): () => Boolean = {
        def in_range(bunny : Bunny) : Boolean = {
            return((bunny.pos - tower.pos).norm <= tower.range &&
                    tower.owner != bunny.owner)
        }
        def fire_at(targets : ListBuffer[Bunny]): Unit = {
            targets.foreach( _.remove_hp(damage, owner) )
            gamestate.tower_fire_strategy(tower)
            gamestate -= tower
        }
        def in_radius(bunny : Bunny) : Boolean = {
            return((bunny.pos - tower.pos).norm <= tower.range &&
                    tower.owner != bunny.owner)
        }
        def attack(): Boolean = {
            if (!gamestate.bunnies.filter(_.alive).filter(in_range).isEmpty) {
                val targets = gamestate.bunnies.filter(_.alive).filter(in_radius)
                fire_at(targets)
                return true
            }
            return false
        }
        return attack
    }
    override def serialize() : Int = -1
}


