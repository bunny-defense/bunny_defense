
package runtime

import collection.mutable.Stack

import game_mechanics.Bunny

/* The spawn scheduler takes care of spawning ennemies in the order and timing set */
object SpawnScheduler
{
  var started     = false
  var spawn_stack = new Stack[(Double,Bunny)]
  var spent_time  = 0.0
  def start(): Unit = {
    started    = true
  }

  def update(dt: Double): Unit = {
    if( started )
    {
      spent_time += dt
      while( !spawn_stack.isEmpty && spawn_stack.top._1 <
            spent_time)
      {
        Controller += spawn_stack.pop()._2.copy()
        println( "Bunny spawned" )
      }
    }
  }

  def set_schedule(schedule: Stack[(Double,Bunny)]): Unit = {
    spawn_stack = schedule.clone()
  }
}
