
package runtime

import collection.mutable.Queue

import game_mechanics.Bunny

/* The spawn scheduler takes care of spawning ennemies in the order and timing set */
object SpawnScheduler
{
  var started     = false
  var spawn_queue= new Queue[(Double,Bunny)]
  var spent_time  = 0.0
  def start(): Unit = {
    started    = true
  }

  def update(dt: Double): Unit = {
    if( started )
    {
      spent_time += dt
      while( !spawn_queue.isEmpty && spawn_queue.head._1 <
            spent_time)
      {
        val bunny = spawn_queue.dequeue._2.copy()
        bunny.update(dt)
        Controller += bunny
        println( "Bunny spawned" )
      }
    }
  }

  def set_schedule(schedule: Queue[(Double,Bunny)]): Unit = {
    spawn_queue= schedule.clone()
  }

  def is_empty(): Boolean = {
    spawn_queue.isEmpty
  }

  def reset_time(): Unit = {
    spent_time = 0.0
  }
}
