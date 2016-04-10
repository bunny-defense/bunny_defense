
package game_mechanics.bunny

import java.io.File
import javax.imageio.ImageIO

import runtime.Controller

/* A badass bunny, really strong, will become the "default" mob in late game */
class BadassBunny extends Bunny
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
  override def on_death(): Unit = {
      for( i <- 0 until 4 )
      {
          val newbunny = BunnyFactory.create(BunnyFactory.NORMAL_BUNNY)
          newbunny.path = this.path.copy()
          Controller += newbunny
      }
  }
}
