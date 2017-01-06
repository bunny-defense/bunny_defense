
package gui

import swing._
import swing.event._

import java.awt.AlphaComposite
import java.awt.image.BufferedImage
import java.awt.MouseInfo
import java.io.File
import javax.imageio.ImageIO

import runtime.TowerDefense
import runtime.GuiGameState
import game_mechanics.GameMap
import game_mechanics.path.{Waypoint,Path,CellPos}
import collection.mutable.ListBuffer

object MapPanel
{
    val cellsize = 32
    val blocked_cell_image =
        ImageIO.read(
            new File(
                getClass().getResource("/UI/Blocked_Cell.png").getPath()))
    val default_size = new CellPos(
        TowerDefense.gui_size.width - 4 * BuildMenu.buttonSize,
        TowerDefense.gui_size.height - TowerInfoPanel.default_size.y )
}

/* Represents the map on the screen */
class MapPanel(parent: Option[TDComponent], gamestate: GuiGameState)
extends TDComponent(parent)
{
    import MapPanel._
    val map      = gamestate.map
    val rows     = map.height
    val cols     = map.width
    val width    = map.width  * MapPanel.cellsize
    var viewpos : Waypoint = new Waypoint(0,0)
    var darkness = 0f
    val bases = new ListBuffer[CellPos]
    val background_color = Colors.black
    gamestate.players.foreach( bases += _.base )
    size = default_size

    override def on_event(event: Event) : Unit = {
        super.on_event(event)
        event match {
            case MouseClicked (_,p,_,_,_) => {
                val mousex = p.x
                val mousey = p.y
                if( mousex >= 0 && mousey >= 0 &&
                    mousex < size.x && mousey < size.y )
                    on_cell_clicked( new CellPos(
                        (mousex + viewpos.x.toInt) / cellsize,
                        (mousey + viewpos.y.toInt) / cellsize ) )
            }
            case _ => {}
        }
    }
    def on_cell_clicked( pos: CellPos ) : Unit = {
        // Placing a new tower
        if( gamestate.selected_tower != None )
        {
            if( map.valid(pos) )
            {
                if( gamestate.player.remove_gold(
                    gamestate.selected_tower.get.price) )
                    { println("Placing Tower")
                        gamestate.new_tower_strategy(gamestate.selected_tower.get, pos)
                    }
            }

        }
        // Selecting a placed tower
        else
        {
          gamestate.selected_cell = gamestate.towers.find( _.pos == pos )
          if (gamestate.selected_cell != None) {
            gamestate.tower_panel.update_selection(true)
          } else {
            gamestate.tower_panel.update_selection(false)
          }
        }
        // Building multiple towers
        if( !TowerDefense.keymap(Key.Shift) )
            gamestate.selected_tower = None
    }

    def drawLine(g: Graphics2D, x1: Int, y1: Int, x2: Int, y2: Int): Unit = {
        g.drawLine(x1 - viewpos.x.toInt, y1 - viewpos.y.toInt,
                   x2 - viewpos.x.toInt, y2 - viewpos.y.toInt)
    }

    def drawRect(g: Graphics2D, x: Int, y: Int, w: Int, h: Int): Unit = {
        g.drawRect(x - viewpos.x.toInt, y - viewpos.y.toInt, w, h)
    }

    def drawOval(g: Graphics2D, x: Int, y: Int, w: Int, h: Int): Unit = {
        g.drawOval(x - viewpos.x.toInt, y - viewpos.y.toInt, w, h)
    }

    def drawImage(g: Graphics2D, image: BufferedImage, x: Int, y: Int): Unit = {
        g.drawImage(image,
            x - viewpos.x.toInt,
            y - viewpos.y.toInt,
            null)
    }

    def fillRect(g: Graphics2D, x: Int, y: Int, w: Int, h: Int): Unit = {
        g.fillRect(x - viewpos.x.toInt, y - viewpos.y.toInt, w, h)
    }

    def paintPath(g: Graphics2D): Unit = {
        val path = new ListBuffer[Path]()
        for (bunny <- gamestate.bunnies) {
            path += bunny.path.path
        }
        for (j <- path ) {
            for( i <- 1 until j.length )
            {
                val prev = j.at(i-1)
                val curr = j.at(i)
                drawLine(g,
                    prev.x.toInt * cellsize + cellsize / 2,
                    prev.y.toInt * cellsize + cellsize / 2,
                    curr.x.toInt * cellsize + cellsize / 2,
                    curr.y.toInt * cellsize + cellsize / 2 )
            }
        }
    }

    /* Drawing on the map */
    override def draw(g: Graphics2D): Unit = {
        val clip = g.getClip()
        g.clipRect( 0, 0, size.x, size.y )
        g.setColor( background_color )
        g.fillRect(0, 0, size.x, size.y)
        /* Drawing the map */
        drawImage(g, map.map_image, 0, 0)
        paintPath(g)
        /* Drawing ghost tower */
        gamestate.selected_tower match {
            case None => {}
            case Some(tower) => {
                // PAINT NO-PLACE ZONE
                g.setComposite(
                    AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 0.5f ))
                for( x <- 0 until map.width )
                {
                    for( y <- 0 until map.height )
                    {
                        if( map.obstructed(x,y) )
                        {
                            drawImage(g, blocked_cell_image,
                                x * cellsize,
                                y * cellsize)
                        }
                    }
                }
                g.setComposite(
                    AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 1f ))
                g.setColor( Colors.black )
                // PAINT TOWER AND RANGE
                val mousepos  = MouseInfo.getPointerInfo().getLocation()
                val windowpos = locationOnScreen
                val snapx     = (mousepos.x + viewpos.x.toInt - windowpos.x) /
                                cellsize * cellsize
                val snapy     = (mousepos.y + viewpos.y.toInt - windowpos.y) /
                                cellsize * cellsize
                drawRect(g, snapx, snapy, cellsize, cellsize )
                drawImage(g, tower.tower_graphic, snapx, snapy)
                val range   = tower.range * cellsize
                val circlex = snapx + cellsize / 2 - range
                val circley = snapy + cellsize / 2 - range
                drawOval(g, circlex, circley, 2 * range, 2 * range )
            }
        }
        /* Drawing tower effects */
        val translate_transform = g.getTransform()
        for( tower <- gamestate.towers )
        {
            val x = tower.pos.x * cellsize + cellsize / 2 - viewpos.x.toInt
            val y = tower.pos.y * cellsize + cellsize / 2 - viewpos.y.toInt
            g.translate( x, y )
            tower.tower_type.draw_effect(g)
            g.setTransform( translate_transform )
            g.setComposite(
                AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 1f ) )
        }
        /* Drawing the towers */
        for( tower <- gamestate.towers )
        {
            val x = tower.pos.x * cellsize
            val y = tower.pos.y * cellsize
            drawImage(g, tower.graphic, x.toInt, y.toInt)
        }
        /* Drawing the bunnies */
        for( bunny <- gamestate.bunnies )
        {
            val x = bunny.pos.x * cellsize
            val y = bunny.pos.y * cellsize
            drawImage(g, bunny.graphic, x.toInt, y.toInt)
            // Health bar
            if( bunny.hp != bunny.initial_hp )
            {
                val health_ratio = bunny.hp / bunny.initial_hp
                g.setColor( Colors.black )
                drawRect(g, x.toInt - 1, y.toInt - 4,
                    cellsize + 2, 5 )
                g.setColor( Colors.transparent_grey )
                fillRect(g, x.toInt, y.toInt - 3, cellsize, 3)
                g.setColor( Colors.green )
                fillRect(g, x.toInt, y.toInt - 3,
                         (health_ratio * cellsize).toInt, 3)
            }
            g.setColor( Colors.black )
        }
        /* Drawing projectiles */
        for( projectile <- gamestate.projectiles )
        {
            val x = projectile.pos.x * cellsize - viewpos.x.toInt
            val y = projectile.pos.y * cellsize - viewpos.y.toInt
            val angle = Math.atan2(
                projectile.direction.y,
                projectile.direction.x ) - Math.PI / 4
            val prev_transform = g.getTransform()
            g.rotate( angle,
                x + cellsize / 2,
                y + cellsize / 2 )
            g.drawImage(projectile.graphic, x.toInt, y.toInt, null)
            g.setTransform( prev_transform )
        }
        /* Darkness level */
        g.setComposite(
            AlphaComposite.getInstance( AlphaComposite.SRC_OVER, darkness ) )
        g.setColor( Colors.black )
        fillRect(g, 0, 0, map.width * cellsize, map.height * cellsize)
        g.setComposite(
            AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 1f ) )
        for( animation <- gamestate.animations)
        {
            animation.draw(g)
            // Resetting the alpha composite
            g.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 1.0f ) )
        }
        /* Drawing selected tower */
        gamestate.selected_cell match {
            case None => {}
            case Some(tower) => {
                g.setColor(Colors.black)
                drawOval(g,
                    tower.pos.x.toInt * cellsize
                    - tower.range * cellsize
                    + cellsize/2,
                    tower.pos.y.toInt * cellsize
                    - tower.range * cellsize
                    + cellsize/2,
                    tower.range*cellsize*2,
                    tower.range*cellsize*2 )
            }
        }
        for( base <- bases )
        {
            g.setColor( new Color( 200, 200, 200, 100 ) )
            fillRect(g, base.x * cellsize, base.y * cellsize,
                cellsize, cellsize)
        }
        g.setClip(clip)
    }
}
