
package gui

import swing._

import game_mechanics.Player
import runtime.{Controller,SpawnScheduler}

class InfoPanel() extends Panel
{
  background = Colors.lightGrey
  override def paintComponent(g: Graphics2D): Unit = {
    super.paintComponent(g)
    val xm = size.width
    val ym = size.height
    g.drawString("Wave : " + (Controller.wave_counter.toString),
                  (xm/2-34),ym/5+5)
    g.drawString("Remaining Bunnies :"+ (SpawnScheduler.spawn_queue.length),
                  (xm-2/34),2*ym/5+5)
    g.drawString("Gold : " + (Player.gold.toString),
                  (xm/2-34),3*ym/5+5)
    g.drawString("Lives : " + (Player.hp.toString),
                  (xm/2-34),4*ym/5+5)
    /* If other entries are added in the menu :
     Let n be the number of entries, the coordinates of the k-th entry is
     (xm/2)-offset, (k*ym/(n+1))+5 */
  }
}
