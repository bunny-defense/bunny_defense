
package game_mechanics.tower

import collection.mutable.ListBuffer

import game_mechanics.Bunny
import game_mechanics.path.Waypoint
import runtime.Spawner

object BaseTower extends TowerType
{
    def closest_to(bunnies : ListBuffer[Bunny], p: Waypoint): Option[Bunny] = {
        /* Returns the bunny that is the closest to a given point p */
        def distance_comp(x: Bunny,y: Bunny) =
            (x.pos-p).norm < (y.pos-p).norm
        val sorted_bunnies = bunnies.sortWith(distance_comp)
        if (sorted_bunnies.isEmpty) {return None}
        else {return Some(sorted_bunnies.head)}
    }

    override def get_targets(in_range : ListBuffer[Bunny])
        : ListBuffer[Bunny] = {
        closest_to( in_range, Spawner.bunnyend.toFloat ) match {
            case None => new ListBuffer[Bunny]()
            case Some(bunny) => {
                val list = new ListBuffer[Bunny]
                list += bunny
                return list
            }
        }
    }
}

