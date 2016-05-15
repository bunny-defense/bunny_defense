
package game_mechanics

import io.Source

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import utils.Landscape
import game_mechanics.tower.Tower
import game_mechanics.path.CellPos
import runtime.{Spawner,TowerDefense}
import gui.MapPanel
import util.Random

class GameMap(width0: Int, height0: Int)
{
    val law = new Random()
    val width           = width0
    val height          = height0
    val graphic_map     = Array.ofDim[BufferedImage](width,height)
    val obstruction_map = Array.ofDim[Boolean](width,height)
    val base_ground_image =
        ImageIO.read(
            new File(getClass().getResource("/ground/rock1.png").getPath()))

    val landscape_top    = Landscape.generate( width, height / 3 )
    val landscape_bottom = Landscape.generate( width, height / 3 )

    val map_image = new BufferedImage(
        width * MapPanel.cellsize,
        height * MapPanel.cellsize, base_ground_image.getType() )

    for( x <- 0 until width ) {
        for( y <- 0 until height ) {
            obstruction_map(x)(y) = false
            if( y < landscape_top(width - 1 - x)
                || height - y - 1 < landscape_bottom(x) )
            {
                val ground_id = law.nextInt(4)+1
                val ground_image =
                    ImageIO.read(
                        new File(getClass().getResource("/ground/rock" + ground_id.toString + ".png").getPath()))
                map_image.getGraphics().drawImage( ground_image,
                    x * MapPanel.cellsize, y * MapPanel.cellsize, null )
                graphic_map(x)(y) = ground_image
                obstruction_map(x)(y) = true
            }
            else
            {
                val grass_id = law.nextInt(8)+1
                val grass_image  =
                    ImageIO.read(
                        new File(getClass().getResource("/ground/grass" + grass_id.toString + ".png").getPath()))
                map_image.getGraphics().drawImage( grass_image,
                    x * MapPanel.cellsize, y * MapPanel.cellsize, null )
                graphic_map(x)(y) = grass_image
            }
        }
    }

    def +=(tower: Tower): Boolean = {
        if( !obstruction_map(tower.pos.x)(tower.pos.y) )
        {
            obstruction_map(tower.pos.x)(tower.pos.y) = true
            val jps = new JPS( Spawner.bunnystart, Spawner.bunnyend)
            jps.run() match
            {
                case Some(path) => {
                    return true
                }
                case None => {
                    obstruction_map(tower.pos.x)(tower.pos.y) = false
                    println( "You are blocking the way" )
                }
            }
        }
        return false
    }

    def -=(tower: Tower): Unit = {
        obstruction_map(tower.pos.x)(tower.pos.y) = false
    }

    def on_map(x:Int, y: Int) : Boolean = {
        -1 <= x && x <= this.width && 0 <= y && y < this.height
    }

    def obstructed(x: Int, y: Int) : Boolean = {
        if (!on_map(x,y)) {
            return true
        }
        if( x == -1 || x == this.width ) {
            return false
        }
        obstruction_map(x)(y)
    }

    def valid( pos : CellPos ): Boolean = {
        // Cannot place tower on top of bunnies
        if( TowerDefense.gamestate.bunnies.count( _.pos.toInt == pos ) > 0 )
            return false
        // Out of bounds
        if( pos.x < 0 || pos.y < 0 || pos.x >= width || pos.y >= height )
            return false
        // Obstructed
        if( obstruction_map( pos.x )( pos.y ) )
            return false
        obstruction_map( pos.x )( pos.y ) = true
        val jps = new JPS( Spawner.bunnystart, Spawner.bunnyend )
        val result = jps.run() match {
            case None    => false
            case Some(_) => true
        }
        obstruction_map( pos.x )( pos.y ) = false
        return result
    }
}
