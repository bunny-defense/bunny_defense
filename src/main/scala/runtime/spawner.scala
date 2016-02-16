package runtime

import game_mechanics.Bunny
import game_mechanics._
import game_mechanics.path._
import runtime._
import collection.mutable.Queue
import collection.immutable.Map
import util.Random

object Spawner
{
  val bunnystart = new Waypoint(0,5)
  val bunnyend = new Waypoint(30,5)
}

class Spawner(id: Int) {
  import Spawner._
  val src = scala.io.Source.fromFile("src/main/resources/waves/wave"+id.toString+".csv")
  val iter = src.getLines().map(_.split(","))
  var spawn_scheduler = new Queue[(Double,Bunny)]
  val test_path = new Path
  test_path += bunnystart
  test_path += bunnyend

  val law = new Random()

  val mappage: Map[String, Bunny] = Map(
    "Bunny" -> (new Bunny (new Progress(test_path))),
    "Heavy_Bunny" -> (new Heavy_Bunny (new Progress(test_path))),
    "Hare"-> (new Hare (new Progress(test_path))),
    "Otter"-> (new Otter (new Progress(test_path))),
    "Golden_Bunny" -> (new Golden_Bunny (new Progress(test_path)))
  )
  def create(): Queue[(Double,Bunny)]= {
    for (appear <- iter) {
      if (law.nextDouble > 1/1000) {
      spawn_scheduler += ((((appear(0)).toDouble),
                          mappage(appear(1).trim)))
                      }
      else {
        spawn_scheduler += (((appear(0)).toDouble,
                            mappage("Golden_Bunny")))
                      }
    }
    return(spawn_scheduler)
  }

}
