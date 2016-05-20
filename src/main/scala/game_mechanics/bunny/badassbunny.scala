
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
    _path: Progress,
    gamestate: GameState)
extends Bunny(_owner, _path, gamestate)
{
  override val id          = bunny_id
  override val bunny_graphic =
    ImageIO.read(
      new File(getClass().getResource("/mobs/badassbunny.png").getPath()))
  pos = path.path.head
  initial_hp               = 30.0
  override val base_shield = 2.0
  shield                   = 2.0
  base_speed               = 1.5
  speed                    = 1.5
  override def reward      = atan_variation(15,3,15)
  override def on_death(): Unit = {
      for( i <- 0 until 4 )
      {
          val path = new JPS(this.path.get_position().toInt,
              this.path.last.toInt, gamestate)
              .run() match {
                  case Some(p) => p
                  case None => throw new Exception()
              }
          val newbunny = BunnyFactory.create(
              BunnyFactory.NORMAL_BUNNY,
              owner,
              new Progress( path ),
              this.gamestate
          )
          gamestate += newbunny
      }
  }
}
