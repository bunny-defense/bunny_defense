
package runtime

import collection.mutable.{ListBuffer,Stack}

import game_mechanics.{Bunny,Throw,Tower}
import game_mechanics.path._

object Controller
{
  val bunnies     = new ListBuffer[Bunny]
  val projectiles = new ListBuffer[Throw]
  val towers      = new ListBuffer[Tower]

    val spawn_schedule = new Stack[(Double,Bunny)]
    val testpath = new Path
    testpath += new Waypoint(0,0)
    testpath += new Waypoint(10,10)
    spawn_schedule.push( (0.00, new Bunny( new Progress(testpath))) )
    SpawnScheduler.set_schedule( spawn_schedule )
    SpawnScheduler.start()

  def update(dt: Double): Unit = {
    /* Update projectiles */
    for( projectile <- projectiles )
      projectile.update(dt)
    /* Update towers */
    for( tower <- towers )
      tower.update(dt)
    /* Update bunnies */
    for( bunny <- bunnies )
      bunny.update(dt)
    /* Spawn in new bunnies */
    SpawnScheduler.update(dt)
  }

  /* BUNNIES */

  def +=(bunny: Bunny): Unit = {
    bunnies += bunny
  }

  def -=(bunny: Bunny): Unit = {
    bunnies -= bunny
  }

  /* PROJECTILES */

  def +=(projectile: Throw): Unit = {
    projectiles += projectile
  }

  def -=(projectile: Throw): Unit = {
    projectiles -= projectile
  }

  /* TOWERS */

  def +=(tower: Tower): Unit = {
    towers += tower
  }

  def -=(tower: Tower): Unit = {
    towers -= tower
  }
}
