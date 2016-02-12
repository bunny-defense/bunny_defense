package runtime

import game_mechanics.Bunny
import game_mechanics._
import game_mechanics.path._
import runtime._
import collection.mutable.Queue
import collection.immutable.Map
class Spawner(id: Int) {

  val src = scala.io.Source.fromFile("src/main/resources/waves/wave"+id.toString+".csv")
  val iter = src.getLines().map(_.split(","))
  var spawn_scheduler = new Queue[(Double,Bunny)]
  val mappage: Map[Int,Bunny] = Map(
    1-> (new Bunny (new Progress(test_path))),
    2-> (new Heavy_Bunny (new Progress(test_path))),
    3-> (new Hare (new Progress(test_path))),
    4-> (new Otter (new Progress(test_path)))
  )
  val test_path = new Path
  test_path += new Waypoint(0,5)
  test_path += new Waypoint(30,5)

  def create(): Queue[(Double,Bunny)]= {
    for (appear <- iter) {
      spawn_scheduler += ((((appear(0)).toDouble),
        mappage(appear(1).toInt)))
    }
    /* SpawnScheduler.set_schedule(spawn_scheduler) */
    return(spawn_scheduler)
  }

}
