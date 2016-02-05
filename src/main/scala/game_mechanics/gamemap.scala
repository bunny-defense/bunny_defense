
package game_mechanics

import io.Source

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class GameMap(width0: Int, height0: Int)
{
  val width  = width0
  val height = height0
  val graphic_map = Array.ofDim[BufferedImage](width,height)
  val obstruction_map = Array.ofDim[Boolean](width,height)

  val io = new File(getClass().getResource("/bunny_icon.png").getPath())
  println( io )
  val test_image = ImageIO.read(io)

  for( x <- 0 until width ) {
    for( y <- 0 until height ) {
      graphic_map(x)(y) = test_image
    }
  }
}
