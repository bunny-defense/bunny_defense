
package game_mechanics.path

import util.Random
import Math._

class Progress(p: Path) {
    /* Index of the next node */
    var i = 1
    var law = new Random()

    /* Distance traveled along the path [current node] -> [next node] */
    var progress = 0.0
    var path = p

    def move( distance: Double ): Unit = {
        if( reached )
            return
        val current = path.at(i-1)
        val next    = path.at(i)
        val dist    = current distance_to next
        val lastprog = progress
        /* If we have reached the next node we reset progress along the line and go to the next one */
        if( (dist - progress) < distance ) {
            val rest = distance - (dist - progress)
            progress = 0.0
            i += 1
            move( rest )
        }
        else { /* else we just move along the line */
            progress += distance
        }
    }

    def get_position(): Waypoint = {
        if( reached )
            return path.last
        val current = path.at(i-1)
        val next    = path.at(i)
        val ratio   = progress / ( current distance_to next )
        return current * ( 1 - ratio ) + next * ratio
        }

    def reached(): Boolean = {
        return i >= path.length()
    }

    def random_choice() : Unit = {
        i += Math.min(law.nextInt(path.length - i - 6),5)
    }

    def reset() : Unit = {
        this.i = 1
        this.progress = 0.0
        this.law = new Random()
    }

    def copy() : Progress = {
        val newprogress = new Progress( path )
        newprogress.progress = progress
        newprogress.i = i
        return newprogress
    }

}
