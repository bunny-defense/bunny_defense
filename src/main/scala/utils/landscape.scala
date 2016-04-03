
package utils

import util.Random

object Landscape
{
    val rng = new Random()

/*
    def generate(width: Int, max_height: Int): Array[Int] = {
        val power = (Math.log(width.toDouble)/Math.log(2.0)).toInt + 1
        val newwidth = Math.pow( 2, power.toDouble ).toInt // Why is there no integer exponentiation in Scala ? :(
        val array = new Array[Int](newwidth)
        def get(i: Int): Int = {
            try{
                array(i)
            } catch {
                case e : IndexOutOfBoundsException => 0
            }
        }
        array(1) = 0
        array(newwidth/2) = rng.nextInt( max_height + 1 )
        var d = newwidth / 4
        while( d > 0 )
        {
            var i = 1
            for( i <- 0 until (newwidth / (2 * d)) )
            {
                val a = array(2 * i * d)
                val b = get(2 * (i+1) * d)
                val mid = a + b / 2
                array( d + 2 * i * d ) = mid - 1 + rng.nextInt( 3 )
            }
            d /= 2
        }
        val result = new Array[Int](width)
        var i = 0
        for( i <- 0 until width - 1 )
        {
            result(i) = array( (i.toDouble / (width-1) * (newwidth-1)).toInt )
        }
        return result
    }
*/
    def generate(width: Int, max_height: Int): Array[Int] = {
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
}
