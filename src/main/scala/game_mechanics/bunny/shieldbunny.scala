
package game_mechanics.bunny

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import runtime.GameState
import game_mechanics.JPS
import game_mechanics.Player
import game_mechanics.path._

case class ShieldBunny(
    _owner: Player,
    bunny_id: Int,
    start: CellPos,
    _target : Player,
    gamestate: GameState)
extends Bunny(_owner,gamestate)
{
    override val id           = bunny_id
    override val target       = _target
    override var path = new Progress(
        new JPS(start, target.base, gamestate).run()
                    match {
                        case None    => throw new Exception()
                        case Some(p) => p
                    }
                    )
    override val bunny_graphic =
        ImageIO.read(
            new File(getClass().getResource("/mobs/shield_bunny.png").getPath()))
    override val effect_range = 2
    val shield_increase       = 1.5
    override def allied_effect(bunny: Bunny): Unit = {
        bunny.shield *= 1.5
    }
}
