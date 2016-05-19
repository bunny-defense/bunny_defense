
package game_mechanics.bunny

import java.io.File
import javax.imageio.ImageIO

import game_mechanics.path._
import game_mechanics.Player
import game_mechanics.JPS

import runtime.TowerDefense
import runtime.GameState

/* A badass bunny, really strong, will become the "default" mob in late game */
case class BadassBunny(
    _owner: Player,
    bunny_id: Int,
    start: CellPos,
    _target: Player,
    gamestate: GameState)
extends Bunny(_owner,gamestate)
{
  override val id          = bunny_id
  override val target      = _target
  override val bunny_graphic =
    ImageIO.read(
      new File(getClass().getResource("/mobs/badassbunny.png").getPath()))
  override var path = new Progress(
      new JPS(start, target.base, gamestate).run()
      match {
          case None    => throw new Exception()
          case Some(p) => p
      }
      )
  initial_hp               = 30.0
  override val base_shield = 2.0
  shield                   = 2.0
  base_speed               = 1.5
  speed                    = 1.5
  override def reward      = atan_variation(15,3,15)
  override def on_death(): Unit = {
      for( i <- 0 until 4 )
      {
          val newbunny = BunnyFactory.create(
              BunnyFactory.NORMAL_BUNNY,
              owner,
              this.path.get_position().toInt,
              target,
              this.gamestate
          )
          gamestate += newbunny
      }
  }
}
