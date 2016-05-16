
package game_mechanics.bunny

import java.io.File
import javax.imageio.ImageIO

import game_mechanics.path._
import game_mechanics.JPS

import runtime.TowerDefense

/* A badass bunny, really strong, will become the "default" mob in late game */
case class BadassBunny(player_id: Int, bunny_id: Int, pos: CellPos, arrival : CellPos) extends Bunny
{
  override val id          = bunny_id
  override val player      = player_id
  override val bunny_graphic =
    ImageIO.read(
      new File(getClass().getResource("/mobs/badassbunny.png").getPath()))
  override val path = new Progress(
        new JPS(pos, arrival).run()
                    match {
                        case None    => throw new Exception()
                        case Some(p) => p
                    }
                    )
  override val initial_hp  = 30.0
  override val base_shield = 2.0
  shield                   = 2.0
  override val base_speed  = 1.5
  speed                    = 1.5
  override val price       = 50
  override def reward      = atan_variation(15,3,15)
  override def on_death(): Unit = {
      for( i <- 0 until 4 )
      {
          val newbunny = BunnyFactory.create(
              BunnyFactory.NORMAL_BUNNY,
              player,
              this.path.get_position().toInt,
              this.path.path.last.toInt
          )
          TowerDefense.gamestate += newbunny
      }
  }
}
