
package runtime

import swing._
import swing.event._
import collection.mutable.Queue

import game_mechanics.Bunny

case object WaveStarted extends Event
case object WaveEnded   extends Event

/* The spawn scheduler takes care of spawning ennemies in the order and timing set */
object SpawnScheduler extends Publisher
{
  var started     = false
  var spawn_queue = new Queue[(Double,Bunny)]
  var spent_time  = 0.0

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
        Controller += spawn_queue.dequeue._2.copy()
      if( spawn_queue.isEmpty && Controller.bunnies.isEmpty )
      {
        started = false
        publish( WaveEnded )
      }
    }
  }

  def set_schedule(schedule: Queue[(Double,Bunny)]): Unit = {
    spawn_queue = schedule.clone()
  }

  def is_empty(): Boolean = {
    spawn_queue.isEmpty
  }

  def reset_time(): Unit = {
    spent_time = 0.0
  }
}
