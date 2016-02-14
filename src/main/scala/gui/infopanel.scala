
package gui

import swing._

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import game_mechanics.Player
import runtime.Controller
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
  background = Colors.lightGrey
  override def paintComponent(g: Graphics2D): Unit = {
    super.paintComponent(g)
    val xm = size.width
    val ym = size.height
    g.drawString("Wave : " + (Controller.wave_counter.toString),(xm/2-34),ym/4+5)
    g.drawString("Gold : " + (Player.gold.toString),(xm/2-34),2*ym/4+5)
    g.drawString("Lives : " + (Player.hp.toString),(xm/2-34),3*ym/4+5)
    /* If other entries are added in the menu :
     Let n be the number of entries, the coordinates of the k-th entry is
     (xm/2)-offset, (k*ym/(n+1))+5 */
  }
}
