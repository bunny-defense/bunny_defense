
package runtime

import swing._
import swing.event._
import collection.mutable.Queue
import util.Random

import game_mechanics._
import game_mechanics.path._
import game_mechanics.tower._
import game_mechanics.bunny._

case object WaveStarted extends Event
case object WaveEnded   extends Event

/* The spawn scheduler takes care of spawning ennemies in the order and timing
 * set */
object SpawnScheduler extends Publisher
{
    var started     = false
    var spawn_queue = new Queue[(Double,Int)]
    var spent_time  = 0.0
    val law         = new Random()

    def start(): Unit = {
        reset_time
        started    = true
        publish( WaveStarted )
    }

    def update(dt: Double, gamestate : AloneGameState): Unit = {
        if( started )
        {
            spent_time += dt
            while( !spawn_queue.isEmpty && spawn_queue.head._1 < spent_time) {
                val path = new JPS(new CellPos (-1, law.nextInt(gamestate.map_panel.map.height)),
                    new CellPos (gamestate.map_panel.map.width,
                        law.nextInt(gamestate.map_panel.map.height/2)+
                        gamestate.map_panel.map.height/4),
                        gamestate.map).run()
                match {
                    case None     => throw new Exception()
                    case Some(p)  => new Progress(p)
                }
                gamestate += BunnyFactory.create(
                    spawn_queue.dequeue._2,
                    gamestate.enemy,
                    path,
                    gamestate
                    )
            }
            if( spawn_queue.isEmpty && gamestate.bunnies.isEmpty )
            {
              started = false
              publish( WaveEnded )
            }
        }
    }

    def set_schedule(schedule: Queue[(Double,Int)]): Unit = {
        spawn_queue = schedule.clone()
    }

    def is_empty(): Boolean = {
        spawn_queue.isEmpty
    }

    def reset_time(): Unit = {
        spent_time = 0.0
    }
}
