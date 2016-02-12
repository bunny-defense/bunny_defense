
package gui

import swing._

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import game_mechanics.Player

object InfoPanel
{
  val wave = 1
  /* Number of the current wave */
  val rem_bunnies = 50
  /* The number of remaining bunnies for the current wave */
}

class InfoPanel() extends Panel
{
  import InfoPanel._
  background = Colors.blue
  override def paintComponent(g: Graphics2D): Unit = {
    val xm = size.width
    val ym = size.height
    g.drawString("Gold : " + (Player.gold.toString), (xm/2), ym/3)
    g.drawString("Lives : " + (Player.hp.toString), (xm/2), 2*ym/3)
    /* If other entries are added in the menu :
     Let n be the number of entries, the coordinates of the k-th entry is
     (xm/2), (k*ym/(n+1)) */
  }
}
