
package game_mechanics.path

import util.Random
import Math._

class Progress(p: Path) {
    /** A class that defines and manages a path */

    /* Index of the next node */
    var i = 1
    /* Random law useful for the SpecOpsBunny */
    var law = new Random()

    /* Distance traveled along the path [current node] -> [next node] */
    var progress = 0.0
    var path = p

    def move( distance: Double ): Unit = {
        if( reached )
            return
        val current = path.head
        val next    = path.apply(1)
        val dist    = current distance_to next
        val lastprog = progress
        /* If we have reached the next node we reset progress along the line and go to the next one */
        if( (dist - progress) < distance ) {
            val rest = distance - (dist - progress)
            progress = 0.0
            path.pop
            move( rest )
        }
        else { /* else we just move along the line */
            progress += distance
        }
    }

    def get_position(): Waypoint = {
        if( reached )
            return path.last
        val current = path.head
        val next    = path.apply(1)
        val ratio   = progress / ( current distance_to next )
        return current * ( 1 - ratio ) + next * ratio
        }

    def reached(): Boolean = {
        return path.length == 1
    }

    def random_choice() : Unit = {
        if (!reached) {
            path.takeRight(path.length - Math.max(law.nextInt(path.length - i),12) -6)
        }
    }

    def reset() : Unit = {
        /** Resets some attributes of the path, used when the path changes */
        this.progress = 0.0
        this.law = new Random()
    }

    def copy() : Progress = {
        val newprogress = new Progress( path )
        newprogress.progress = progress
        return newprogress
    }

    override def toString(): String = {
        return path.toString
    }

    def last(): Waypoint = {
        return path.last
    }
}
