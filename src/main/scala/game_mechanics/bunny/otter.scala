
package game_mechanics.bunny

import java.io.File
import javax.imageio.ImageIO

import runtime.GameState
import game_mechanics.JPS
import game_mechanics.Player
import game_mechanics.path._

/* A boss! */
case class Otter(
    _owner: Player,
    bunny_id: Int,
    start: CellPos,
    _target : Player,
    gamestate: GameState)
extends Bunny(_owner,gamestate)
{
    override val id            = bunny_id
    override val target        = _target
    override var path = new Progress(
        new JPS(start, target.base, gamestate).run()
                    match {
                        case None    => throw new Exception()
                        case Some(p) => p
                    }
                    )
    override val bunny_graphic =
        ImageIO.read(
            new File(getClass().getResource("/mobs/otter.png").getPath()))
    initial_hp                 = 1000.0
    override val base_shield   = 1.5
    shield                     = 1.5
    base_speed                 = 1.0
    speed                      = 1.0
    override val damage        = 5
    override def reward        = atan_variation(100,15,25)
}
