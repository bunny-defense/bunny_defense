package runtime

import game_mechanics._
import game_mechanics.path._
import runtime._
import collection.mutable.Queue

class Spawner(id: Int) {

  val src = scala.io.Source.fromFile("src/main/resources/waves/wave"+id.toString+".csv")
  val iter = src.getLines().map(_.split(","))
  var spawn_scheduler = new Queue[(Double,Bunny)]

  val test_path = new Path
  test_path += new Waypoint(0,5)
  test_path += new Waypoint(30,5)

  def create(): Queue[(Double,Bunny)]= {
    for (appear <- iter) {
      spawn_scheduler += ((((appear(0)).toDouble),
        new Bunny (new Progress(test_path))))
    }
    /* SpawnScheduler.set_schedule(spawn_scheduler) */
    return(spawn_scheduler)
  }

}
