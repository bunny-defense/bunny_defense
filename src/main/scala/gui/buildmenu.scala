
package gui

import swing._
import swing.event._

import java.awt.AlphaComposite
import java.awt.Graphics2D
import java.awt.MouseInfo

import runtime._
import game_mechanics._
import game_mechanics.path._
import game_mechanics.tower._
import gui.animations.UnlockAnimation

object BuildMenu
{
    val buttonSize = 64
}

class BuildMenu (
    parent: Option[TDComponent], gamestate: ClientGameState,
    cols: Int, rows: Int)
extends TDComponent(parent) with Reactor
{
    import BuildMenu._
    val width  = cols * buttonSize
    val height = rows * buttonSize
    size = new CellPos( width, height )

    val towerlist = Array.fill[Option[TowerType]](cols*rows)(None)
    towerlist(0) = Some(BaseTower)
    towerlist(1) = Some(QuickTower)
    towerlist(2) = Some(HeavyTower)
    towerlist(3) = Some(ScarecrowTower)
    towerlist(4) = Some(SplashTower)
    towerlist(5) = Some(Roberto)
    towerlist(6) = Some(SuppBuffTower)
    towerlist(7) = Some(SuppSlowTower)
    towerlist(8) = Some(BaseSpawnerTower)
    towerlist(9) = Some(QuickSpawnerTower)
    towerlist(10)= Some(HareSpawnerTower)
    towerlist(11)= Some(HeavySpawnerTower)
    towerlist(12)= Some(SupportSpawnerTower)
    towerlist(13)= Some(OtterSpawnerTower)

    //towerlist(7) = Some(RaygunTower)
    towerlist(15) = Some(Wall)

    class BuyButton(towertype: Option[TowerType])
    extends TDButton(Some(this))
    {
        size = new CellPos( buttonSize, buttonSize )
        val tower_type = towertype
        override def draw_contents(g: Graphics2D): Unit = {
            towertype match {
                case None =>
                    /* BACKGROUND */
                    g.setColor( Colors.white )
                    g.fillRect( 0, 0, buttonSize, buttonSize )
                case Some(tower) =>
                    var ratio = gamestate.player.gold.toDouble / tower.price.toDouble
                    if( ratio > 1.0 )
                        ratio = 1.0
                    /* BACKGROUND */
                   if( ratio < 1.0 )
                        g.setColor( Colors.lightred )
                    else
                        g.setColor( Colors.white )
                    g.fillRect( 0, 0, buttonSize, buttonSize )
                    /* IMAGE */
                    val graphic = tower.tower_graphic
                    g.drawImage( graphic,
                        buttonSize / 2 - graphic.getWidth() / 2,
                        0, null )
                    /* COST DISPLAY */
                    val sep_height = buttonSize * 3 / 5
                    g.setColor( Colors.red )
                    g.fillRect( 0, sep_height,
                        buttonSize, buttonSize - sep_height )
                    g.setColor( Colors.green )
                    g.fillRect( 0, sep_height,
                        (buttonSize * ratio).toInt, buttonSize - sep_height )
                    val string = tower.price.toString
                    val strwidth = g.getFontMetrics().stringWidth( string )
                    val strheight = (buttonSize + sep_height) / 2
                    /* SEPARATOR */
                    g.setColor( Colors.black )
                    g.drawLine( 0, sep_height, buttonSize, sep_height )
                    /* PRICE */
                    g.drawString( string,
                        buttonSize / 2 - strwidth / 2, strheight )
                    if( gamestate.wave_counter < tower.unlock_wave )
                    {
                        g.setColor( Colors.transparent_grey )
                        g.fillRect( 0, 0, buttonSize, buttonSize )
                        g.setColor( Colors.yellow )
                        val waves_left = tower.unlock_wave - gamestate.wave_counter
                        val wave_string = waves_left.toString
                        val wave_string_width =
                            g.getFontMetrics().stringWidth(wave_string)
                        g.drawString(wave_string,
                            buttonSize / 2 - wave_string_width / 2, 25 )
                    }
            }
            /* OUTLINE */
            g.setColor( Colors.black )
            g.drawRect( 0, 0, buttonSize-1, buttonSize-1 )
        }
        override def toString : String = {
            "build_button " + pos.toString + " " + towertype.toString
        }
    }

    for( x <- 0 until cols )
    {
        for( y <- 0 until rows )
        {
            new BuyButton(towerlist(x + y * cols))
            {
                pos  = new CellPos( x * buttonSize, y * buttonSize )
                size = new CellPos( buttonSize, buttonSize )
                override def action() : Unit = {
                    tower_type match {
                        case None => ()
                        case Some(tower) =>
                        {
                            if( gamestate.wave_counter >= tower.unlock_wave )
                                gamestate.selected_tower = tower_type
                        }
                    }
                }
            }
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
}
