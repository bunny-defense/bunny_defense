
package runtime

import collection.mutable.Stack

import game_mechanics.Bunny

/* The spawn scheduler takes care of spawning ennemies in the order and timing set */
object SpawnScheduler
{
  var start_time  = System.nanoTime
  var started     = false
  var spawn_stack = new Stack[(Double,Bunny)]

  def start(): Unit = {
    start_time = System.nanoTime
    started    = true
  }

  def update(dt: Double): Unit = {
    if( started )
    {
      while( !spawn_stack.isEmpty && spawn_stack.top._1 < (System.nanoTime - start_time) )
      {
        Controller += spawn_stack.pop()._2
        println( "Bunny spawned" )
      }
    }
  }

  def set_schedule(schedule: Stack[(Double,Bunny)]): Unit = {
    spawn_stack = schedule.clone()
  }
}
