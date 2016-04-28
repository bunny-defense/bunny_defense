
package gui

import swing._
import swing.event._


import java.awt.AlphaComposite
import java.awt.image.BufferedImage
import java.awt.MouseInfo
import java.io.File
import javax.imageio.ImageIO

import runtime.{Controller,Spawner}
import game_mechanics.GameMap
import game_mechanics.path.{Waypoint,Path}
import collection.mutable.ListBuffer

object MapPanel
{
    val cellsize = 32
    val blocked_cell_image =
        ImageIO.read(
            new File(
                getClass().getResource("/UI/Blocked_Cell.png").getPath()))
}

/* Represents the map on the screen */
class MapPanel(map0: GameMap) extends Panel {
    import MapPanel._
    val map      = map0
    val rows     = map.height
    val cols     = map.width
    val width    = map.width  * MapPanel.cellsize
    var viewpos : Waypoint = new Waypoint(0,0)
    var darkness = 0f

    preferredSize = new Dimension(
        cellsize * cols,
        cellsize * rows )

    listenTo(mouse.clicks)

    reactions += {
        case e: MouseClicked =>
            Controller.on_cell_clicked(
                e.point.x / cellsize,
                e.point.y / cellsize )
    }

    def paintPath(g: Graphics2D): Unit = {
        val path = new ListBuffer[Path]()
        for (bunny <- Controller.bunnies) {
            path += bunny.path.path
        }
        for (j <- path ) {
            for( i <- 1 until j.length )
            {
                val prev = j.at(i-1)
                val curr = j.at(i)
                g.drawLine(
                    prev.x.toInt * cellsize + cellsize / 2,
                    prev.y.toInt * cellsize + cellsize / 2,
                    curr.x.toInt * cellsize + cellsize / 2,
                    curr.y.toInt * cellsize + cellsize / 2 )
            }
        }
    }


    /* Drawing on the map */
    override def paintComponent(g: Graphics2D): Unit = {
        super.paintComponent(g)
        /* Drawing the map */
        g.drawImage( map.map_image,
            -viewpos.x.toInt,
            -viewpos.y.toInt,
            null )
        paintPath(g)
        /* Drawing tower effects */
        val translate_transform = g.getTransform()
        /* Drawing ghost tower */
        Controller.selected_tower match {
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
                            g.drawImage( blocked_cell_image,
                                x * cellsize,
                                y * cellsize, null )
                        }
                    }
                }
                g.setComposite(
                    AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 1f ))
                g.setColor( Colors.black )
                // PAINT TOWER AND RANGE
                val mousepos  = MouseInfo.getPointerInfo().getLocation()
                val windowpos = locationOnScreen
                val snapx     = (mousepos.x - windowpos.x) / cellsize * cellsize
                val snapy     = (mousepos.y - windowpos.y) / cellsize * cellsize
                g.drawRect( snapx, snapy, cellsize, cellsize )
                g.drawImage( tower.tower_graphic, snapx, snapy, null )
                val range   = tower.range * cellsize
                val circlex = snapx + cellsize / 2 - range
                val circley = snapy + cellsize / 2 - range
                g.drawOval( circlex, circley, 2 * range, 2 * range )
            }
        }
        for( tower <- Controller.towers )
        {
            val x = tower.pos.x * cellsize + cellsize / 2
            val y = tower.pos.y * cellsize + cellsize / 2
            g.translate( x, y )
            tower.towertype.draw_effect(g)
            g.setTransform( translate_transform )
            g.setComposite(
                AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 1f ) )
        }
        /* Drawing the towers */
        for( tower <- Controller.towers )
        {
            val x = tower.pos.x * cellsize
            val y = tower.pos.y * cellsize
            g.drawImage( tower.graphic, x.toInt, y.toInt, null )
        }
        /* Drawing the bunnies */
        for( bunny <- Controller.bunnies )
        {
            val x = bunny.pos.x * cellsize
            val y = bunny.pos.y * cellsize
            g.drawImage( bunny.graphic, x.toInt, y.toInt, null )
            // Health bar
            if( bunny.hp != bunny.initial_hp )
            {
                val health_ratio = bunny.hp / bunny.initial_hp
                g.setColor( Colors.black )
                g.drawRect( x.toInt - 1, y.toInt - 4,
                    cellsize + 2, 5 )
                g.setColor( Colors.transparent_grey )
                g.fillRect( x.toInt, y.toInt - 3, cellsize, 3 )
                g.setColor( Colors.green )
                g.fillRect( x.toInt, y.toInt - 3, (health_ratio * cellsize).toInt, 3 )
            }
            g.setColor( Colors.black )
        }
        /* Drawing projectiles */
        for( projectile <- Controller.projectiles )
        {
            val x = projectile.pos.x * cellsize
            val y = projectile.pos.y * cellsize
            val angle = Math.atan2(
                projectile.direction.y,
                projectile.direction.x ) - Math.PI / 4
            val prev_transform = g.getTransform()
            g.rotate( angle,
                x + cellsize / 2,
                y + cellsize / 2 )
            g.drawImage( projectile.graphic, x.toInt, y.toInt, null )
            g.setTransform( prev_transform )
        }
        /* Darkness level */
        g.setComposite(
            AlphaComposite.getInstance( AlphaComposite.SRC_OVER, darkness ) )
        g.setColor( Colors.black )
        g.fillRect( 0, 0, map.width * cellsize, map.height * cellsize )
        g.setComposite(
            AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 1f ) )
        for( animation <- Controller.animations)
        {
            animation.draw(g)
            // Resetting the alpha composite
            g.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 1.0f ) )
        }
        /* Drawing selected tower */
        Controller.selected_cell match {
            case None => {}
            case Some(tower) => {
                g.setColor(Colors.black)
                g.drawOval(tower.pos.x.toInt * cellsize - tower.range * cellsize
                    + cellsize/2,
                    tower.pos.y.toInt * cellsize - tower.range * cellsize
                        + cellsize/2,
                    tower.range*cellsize*2,
                    tower.range*cellsize*2 )
            }
        }
    }
}
