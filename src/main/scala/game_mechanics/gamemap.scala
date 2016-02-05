
package game_mechanics

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class GameMap(width0: Int, height0: Int)
{
  val width  = width0
  val height = height0
  val graphic_map = Array.ofDim[BufferedImage](width,height)
  val obstruction_map = Array.ofDim[Boolean](width,height)

  for( x <- 0 until width ) {
    for( y <- 0 until height ) {
      graphic_map(x)(y) = ImageIO.read(ClassLoader.getSystemResource("bunny_icon.png"))
    }
  }
}
