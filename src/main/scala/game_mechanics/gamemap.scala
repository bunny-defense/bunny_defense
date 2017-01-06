package game_mechanics

import io.Source
import util.Random
import collection.mutable.ListBuffer

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

import utils.Landscape
import game_mechanics.tower.Tower
import game_mechanics.path.CellPos
import game_mechanics.bunny.Bunny
import runtime.TowerDefense
import runtime.GameState
import gui.MapPanel

class GameMap(
    data: Array[Array[Boolean]],
    gamestate: GameState)
{
    val law = new Random()
    val width           = data.size
    val height          = data(0).size
    val graphic_map     = Array.ofDim[BufferedImage](width,height)
    val obstruction_map = data
    val base_ground_image =
        ImageIO.read(
            new File(getClass().getResource("/ground/rock1.png").getPath()))

    val map_image = new BufferedImage(
        width * MapPanel.cellsize,
        height * MapPanel.cellsize, base_ground_image.getType() )

    for( x <- 0 until width ) {
        for( y <- 0 until height ) {
            if( obstruction_map(x)(y) )
            {
                val ground_id = law.nextInt(4)+1
                val ground_image =
                    ImageIO.read(
                        new File(getClass().getResource("/ground/rock" + ground_id.toString + ".png").getPath()))
                map_image.getGraphics().drawImage( ground_image,
                    x * MapPanel.cellsize, y * MapPanel.cellsize, null )
                graphic_map(x)(y) = ground_image
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
            for( player <- gamestate.players )
            {
                val jps = new JPS(
                    player.base,
                    tower.owner.base,
                    gamestate.map)
                jps.run() match
                {
                    case Some(path) => {}
                    case None => {
                        obstruction_map(tower.pos.x)(tower.pos.y) = false
                        println( "You are blocking the way" )
                        return false
                    }
                }
            }
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

    def valid(pos : CellPos): Boolean = {
        // Cannot place tower on top of bunnies
        if( gamestate.bunnies.count( _.pos.toInt == pos ) > 0 ) {
           println("Bunnied")
            return false
        }
        // Out of bounds
        if( pos.x < 0 || pos.y < 0 || pos.x >= width || pos.y >= height ) {
            println("Out of bounds")
            return false
        }
        // Obstructed
        if( obstruction_map( pos.x )( pos.y ) ) {
            println("Obstructed")
            return false
        }
        obstruction_map( pos.x )( pos.y ) = true
        for(player <- gamestate.players)
        {
            val jps = new JPS(
                gamestate.players(0).base,
                player.base,
                gamestate.map)
            jps.run() match {
                case None    => {
                    obstruction_map( pos.x )( pos.y ) = false
                    return false
                }
                case Some(path) => {}
            }
        }
        return true
    }
}
