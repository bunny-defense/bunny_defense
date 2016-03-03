
package runtime

import swing._
import swing.event._
import collection.mutable.Queue

import game_mechanics._
import game_mechanics.path._

case object WaveStarted extends Event
case object WaveEnded   extends Event

/* The spawn scheduler takes care of spawning ennemies in the order and timing set */
object SpawnScheduler extends Publisher
{
    var started     = false
    var spawn_queue = new Queue[(Double,BunnyType)]
    var spent_time  = 0.0

    val test_path = new Path
    test_path += Spawner.bunnystart
    test_path += Spawner.bunnyend

    def start(): Unit = {
        reset_time
        started    = true
        publish( WaveStarted )
    }

    def update(dt: Double): Unit = {
        if( started )
        {
            spent_time += dt
            while( !spawn_queue.isEmpty && spawn_queue.head._1 < spent_time)
                Controller += new Bunny( spawn_queue.dequeue._2, test_path )
            if( spawn_queue.isEmpty && Controller.bunnies.isEmpty )
            {
                started = false
                publish( WaveEnded )
            }
        }
    }

    def set_schedule(schedule: Queue[(Double,BunnyType)]): Unit = {
        spawn_queue = schedule.clone()
    }

    def is_empty(): Boolean = {
        spawn_queue.isEmpty
    }

    def reset_time(): Unit = {
        spent_time = 0.0
    }
}
