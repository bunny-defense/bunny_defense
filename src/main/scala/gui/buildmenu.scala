
package gui

import swing._
import swing.event._

import java.awt.AlphaComposite
import java.awt.Graphics2D
import java.awt.MouseInfo

import runtime._
import game_mechanics._
import game_mechanics.tower._
import gui.animations.UnlockAnimation

object BuildMenu
{
    val buttonSize = 64
}

class BuildMenu(cols: Int, rows: Int) extends Panel
{
    import BuildMenu._
    val width  = cols * buttonSize
    val height = rows * buttonSize

    val towerlist = Array.fill[Option[TowerType]](cols*rows)(None)
    towerlist(0) = Some(BaseTower)
    towerlist(1) = Some(QuickTower)
    towerlist(2) = Some(HeavyTower)
    towerlist(3) = Some(ScarecrowTower)
    towerlist(4) = Some(SplashTower)
    towerlist(5) = Some(Roberto)
    towerlist(6) = Some(SuppBuffTower)
    towerlist(7) = Some(SuppSlowTower)
    //towerlist(7) = Some(RaygunTower)
    towerlist(15) = Some(Wall)

    preferredSize = new Dimension( cols * buttonSize, rows * buttonSize )

    listenTo(mouse.clicks)

    reactions += {
        case e: MouseClicked =>
            val x = e.point.x / buttonSize
            val y = e.point.y / buttonSize
            if( x >= 0 && x < cols &&
                y >= 0 && y < rows )
            {
                val buttontower = towerlist( x + y * cols )
                buttontower match {
                    case None => ()
                    case Some(tower) =>
                    {
                        /* if( Controller.wave_counter >= tower.unlock_wave ) */
                            Controller.selected_tower = towerlist(x + y * cols)
                    }
                }
            }
        case MousePressed(_,_,_,_,_) =>
            clicked = true
        case MouseReleased(_,_,_,_,_) =>
            clicked = false
    }

    listenTo(SpawnScheduler)

    reactions += {
        case WaveEnded =>
        {
            val towertypes = towerlist.collect( Function.unlift( x =>
                    x match
                    {
                        case None => x
                        case Some(towertype) =>
                        {
                            if( towertype.unlock_wave == Controller.wave_counter )
                                x
                            else
                                None
                        }
                    }) )
            def chain_anims(chain : () => Unit, towertype : TowerType) : () => Unit = {
                () =>
                {
                    val anim = new UnlockAnimation(towertype)
                    anim and_then chain
                    Controller += anim
                }
            }
            towertypes.foldLeft(()=>())(chain_anims)()
        }
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
                if( Controller.wave_counter < tower.unlock_wave )
                {
                    g.setColor( Colors.transparent_grey )
                    g.fillRect(
                        x * buttonSize,
                        y * buttonSize,
                        buttonSize,
                        buttonSize )
                    g.setColor( Colors.yellow )
                    val waves_left = tower.unlock_wave - Controller.wave_counter
                    val wave_string = waves_left.toString
                    val wave_string_width =
                        g.getFontMetrics().stringWidth(wave_string)
                    g.drawString(wave_string,
                        x * buttonSize + buttonSize / 2 - wave_string_width / 2,
                        y * buttonSize + 25 )
                }
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

    def draw_tooltip(g : Graphics2D)
    {
        val windowpos = locationOnScreen
        val mousepos  = MouseInfo.getPointerInfo().getLocation()
        val mousex    = mousepos.x - windowpos.x
        val mousey    = mousepos.y - windowpos.y
        val buttonx   = mousex / buttonSize
        val buttony   = mousey / buttonSize
        if( buttonx >= 0 && buttonx < cols && buttony >= 0 && buttony < rows &&
            mousex >= 0 && mousey >= 0 )
        {
            val buttonid  = buttonx.toInt + buttony.toInt * cols
            towerlist(buttonid) match
            {
                case None => {}
                case Some(tower) =>
                {
                    val namesize = g.getFontMetrics().stringWidth( tower.name )
                    val descsize = g.getFontMetrics().stringWidth( tower.desc )
                    val width = Math.max( namesize, descsize ) + 4
                    val height = 34
                    val rect = new Rectangle(
                        mousepos.x.toInt - width, mousepos.y.toInt,
                        width, height )
                    g.setColor( Colors.lightGrey )
                    g.fill( rect )
                    g.setColor( Colors.black )
                    g.draw( rect )
                    g.drawString( tower.name,
                        mousepos.x.toInt - width + 2, mousepos.y.toInt + 15 )
                    g.drawString( tower.desc,
                        mousepos.x.toInt - width + 2, mousepos.y.toInt + 30 )
                }
            }
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
