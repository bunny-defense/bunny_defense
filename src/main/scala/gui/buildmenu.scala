
package gui

import swing._
import swing.event._

import java.awt.AlphaComposite
import java.awt.Graphics2D
import java.awt.MouseInfo

import runtime.Controller
import game_mechanics._
import game_mechanics.tower._

object BuildMenu
{
    val buttonSize = 64
}

class BuildMenu(cols: Int, rows: Int) extends Panel
{
    import BuildMenu._

    val towerlist = Array.fill[Option[TowerType]](cols*rows)(None)
    towerlist(0) = Some(Wall)
    towerlist(1) = Some(BaseTower)
    towerlist(2) = Some(QuickTower)
    towerlist(3) = Some(HeavyTower)
    towerlist(4) = Some(ScarecrowTower)
    towerlist(5) = Some(Roberto)
    towerlist(6) = Some(SuppBuffTower)
    towerlist(7) = Some(SuppSlowTower)


    preferredSize = new Dimension( cols * buttonSize, rows * buttonSize )

    listenTo(mouse.clicks)

    reactions += {
        case e: MouseClicked =>
            val x = e.point.x / buttonSize
            val y = e.point.y / buttonSize
            Controller.selected_tower = towerlist(x + y * cols)
        case MousePressed(_,_,_,_,_) =>
            clicked = true
        case MouseReleased(_,_,_,_,_) =>
            clicked = false
    }

    var clicked = false

    def drawButton(x: Int, y: Int, g: Graphics2D, hovered: Boolean): Unit = {
        val towerslot = towerlist( x + y * cols )
        towerslot match {
            case None => 
                /* BACKGROUND */
                g.setColor( Colors.white )
                g.fillRect(
                    x * buttonSize,
                    y * buttonSize,
                    buttonSize,
                    buttonSize )
            case Some(tower) =>
                var ratio = Player.gold.toDouble / tower.buy_cost.toDouble
                if( ratio > 1.0 )
                    ratio = 1.0
                /* BACKGROUND */
                if( ratio < 1.0 )
                    g.setColor( Colors.lightred )
                else
                    g.setColor( Colors.white )
                g.fillRect(
                    x * buttonSize,
                    y * buttonSize,
                    buttonSize,
                    buttonSize )
                /* IMAGE */
                val graphic = tower.tower_graphic
                g.drawImage( graphic,
                    x * buttonSize + buttonSize / 2 - graphic.getWidth() / 2,
                    y * buttonSize, null )
                /* COST DISPLAY */
                val sep_height = buttonSize * 3 / 5
                g.setColor( Colors.red )
                g.fillRect(
                    x * buttonSize,
                    y * buttonSize + sep_height,
                    buttonSize,
                    buttonSize - sep_height )
                g.setColor( Colors.green )
                g.fillRect(
                    x * buttonSize,
                    y * buttonSize + sep_height,
                    (buttonSize * ratio).toInt,
                    buttonSize - sep_height )
                val string = tower.buy_cost.toString
                val strwidth = g.getFontMetrics().stringWidth( string )
                val strheight = (buttonSize + sep_height) / 2
                /* SEPARATOR */
                g.setColor( Colors.black )
                g.drawLine(
                    x * buttonSize,
                    y * buttonSize + sep_height,
                    (x+1) * buttonSize,
                    y * buttonSize + sep_height )
                /* PRICE */
                g.drawString( string,
                    x * buttonSize + buttonSize / 2 - strwidth / 2,
                    y * buttonSize + strheight )
        }
        /* OUTLINE */
        g.setColor( Colors.black )
        g.drawRect(
            x * buttonSize,
            y * buttonSize,
            buttonSize-1,
            buttonSize-1 )
        /* HOVER EFFECT */
        if( hovered )
        {
            val alpha = if( clicked ) { 0.7f } else { 0.5f }
            g.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, alpha ) )
            g.setColor( Colors.lightblue )
            g.fillRect(
                x * buttonSize,
                y * buttonSize,
                buttonSize,
                buttonSize )
            g.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 1.0f ) )
        }
    }

    override def paintComponent(g: Graphics2D): Unit = {
        var x = 0
        var y = 0
        for( x <- 0 until cols )
        {
            for( y <- 0 until rows )
            {
                val windowpos = locationOnScreen
                val mousepos  = MouseInfo.getPointerInfo().getLocation()
                val mousex    = mousepos.x - windowpos.x
                val mousey    = mousepos.y - windowpos.y
                val hovered   = (x == mousex / buttonSize) && (y == mousey/buttonSize) &&
                (mousex >= 0) && (mousey >= 0)
                drawButton(x,y,g,hovered)
            }
        }
    }

}
