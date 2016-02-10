
package game_mechanics

import io.Source
import util.Random

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object GameMap
{
  val ground_image = ImageIO.read(new File(getClass().getResource("/ground/dirt.jpg").getPath()))
  val grass_image  = ImageIO.read(new File(getClass().getResource("/ground/grass4.png").getPath()))
}

class GameMap(width0: Int, height0: Int)
{
  import GameMap._
  val width  = width0
  val height = height0
  val graphic_map = Array.ofDim[BufferedImage](width,height)
  val obstruction_map = Array.ofDim[Boolean](width,height)

  val rng = new Random()


  for( x <- 0 until width ) {
    for( y <- 0 until height ) {
      if( rng.nextDouble() < 0.2 )
        graphic_map(x)(y) = ground_image
      else
        graphic_map(x)(y) = grass_image
    }
  }
}
