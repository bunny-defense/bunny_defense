
package game_mechanics

import io.Source

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import utils.Landscape
import game_mechanics.tower.Tower
import game_mechanics.path.CellPos
import runtime.Spawner

object GameMap
{
    val ground_image =
        ImageIO.read(
            new File(getClass().getResource("/ground/dirt.jpg").getPath()))
    val grass_image  =
        ImageIO.read(
            new File(getClass().getResource("/ground/grass4.png").getPath()))
}

class GameMap(width0: Int, height0: Int)
{
    import GameMap._
    val width           = width0
    val height          = height0
    val graphic_map     = Array.ofDim[BufferedImage](width,height)
    val obstruction_map = Array.ofDim[Boolean](width,height)

    val landscape_top    = Landscape.generate( width, height / 3 )
    val landscape_bottom = Landscape.generate( width, height / 3 )

    for( x <- 0 until width ) {
        for( y <- 0 until height ) {
            obstruction_map(x)(y) = false
            if( y < landscape_top(width - 1 - x)
                || height - y - 1 < landscape_bottom(x) )
                graphic_map(x)(y) = ground_image
            else
                graphic_map(x)(y) = grass_image
        }
    }

    def +=(tower: Tower): Boolean = {
        if( !obstruction_map(tower.pos.x)(tower.pos.y) )
        {
            obstruction_map(tower.pos.x)(tower.pos.y) = true
            return true
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
        val jps = new JPS( Spawner.bunnystart, Spawner.bunnyend )
        jps.run() match {
            case None    => return false
            case Some(_) => return true
        }
    }
}
