
package utils

import util.Random

import game_mechanics.path.CellPos

object Landscape
{
    val rng = new Random()
    def random_zigzag(width: Int, max_height: Int): Array[Int] = {
        val array = new Array[Int](width)
        array(0) = rng.nextInt( 2 )
        var i = 0
        while( i < width - 1 )
        {
            while( i < width - 1 && array(i) < max_height )
            {
                i += 1
                array(i) = array(i-1) + rng.nextInt(2)
            }
            while( i < width - 1 && array(i) > 0 )
            {
                i += 1
                array(i) = array(i-1) - rng.nextInt(2)
            }
        }
        return array
    }
    def old_generate(width: Int, height: Int): Array[Array[Boolean]] = {
        val data   = Array.ofDim[Boolean](width, height)
        val top    = random_zigzag(width, height / 3)
        val bottom = random_zigzag(width, height / 3)
        for( x <- 0 until width )
            for( y <- 0 until height )
                data(x)(y) =
                    ( y < top(width - 1 - x) || height - y - 1 < bottom(x) )
        return data
    }
    def generate(width: Int, height: Int,
        players: Int): (Array[Array[Boolean]], Array[CellPos]) = {
            val data = Array.ofDim[Boolean](width, height)
            val bases = Array.ofDim[CellPos](players)
            for( x <- 0 until width )
            {
                data(x)(0) = true
                data(x)(height - 1) = true
            }
            for( y <- 0 until height )
            {
                data(0)(y) = true
                data(width - 1)(y) = true
            }
            for( player <- 0 until players )
            {
                val x = 2 + (width - 5) * (player % 2)
                val y = (player / 2 + 1) * height / (players / 2 + 1)
                bases(player) = new CellPos(x,y)
            }
            return (data,bases)
    }
}
