
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
    val bunny_id: Int,
    _path: Progress,
    _health_modifier: Double = 1.0)
extends Bunny(
    _owner,
    _path,
    _health_modifier)
{
    override val id           = bunny_id
    pos = path.path.head
    override val bunny_graphic =
        ImageIO.read(
            new File(getClass().getResource("/mobs/shield_bunny.png").getPath()))
    override val effect_range = 2
    val shield_increase       = 1.5
    override def allied_effect(bunny: Bunny): Unit = {
        bunny.shield *= 1.5
    }
}
