
package gui

import swing._
import Math._

import game_mechanics.Player._
import game_mechanics.path.CellPos
import runtime.{TowerDefense,SpawnScheduler}

/* An info panel that shows the current gold, HP and wave number */
object InfoPanel
{
    val default_size = new CellPos( 200, 100 )
}

class InfoPanel(parent: Option[TDComponent])
extends TDComponent(parent)
{
    import InfoPanel._
    size = default_size
    //background = Colors.lightGrey
    override def draw(g: Graphics2D): Unit = {
        super.draw(g)
        val xm = size.x
        val ym = size.y
        g.drawString("Wave : " + (TowerDefense.gamestate.wave_counter.toString),
            (xm/2-85),ym/6+5)
        g.drawString("Remaining Bunnies :"+ (SpawnScheduler.spawn_queue.length),
            (xm-2/85),2*ym/6+5)
        g.drawString("Gold : " + (TowerDefense.gamestate.player.gold.toString),
            (xm/2-85),3*ym/6+5)
        g.drawString("Lives : " + ((Math.max(TowerDefense.gamestate.player.hp, 0)).toString),
            (xm/2-85),4*ym/6+5)
        g.drawString("Bunnies slaughtered : "+ (TowerDefense.gamestate.player.killcount.toString),
            (xm/2-85), 5*ym/6+5)
        /* If other entries are added in the menu :
         Let n be the number of entries, the coordinates of the k-th entry is
         (xm/2)-34, (k*ym/(n+1))+5 */
    }
}
