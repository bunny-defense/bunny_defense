
package game_mechanics.bunny

import java.io.File
import javax.imageio.ImageIO
import util.Random

import runtime.TowerDefense
import runtime.GameState
import game_mechanics.path._
import game_mechanics.{Player,JPS}
import gui.animations.{GoldAnimation,SmokeAnimation}

/* Spec Op Bunny */

case class SpecOpBunny(
    _owner: Player,
    val bunny_id: Int,
    _path: Progress,
    _health_modifier: Double = 1.0)
extends Bunny(
    _owner,
    _path,
    _health_modifier)
{
    override val id            = bunny_id
    pos = path.path.head
    override val bunny_graphic =
        ImageIO.read(new File(
            getClass().getResource("/mobs/ninja.png").getPath()))
    override val law           = new Random()
    var jumping                = false
}
