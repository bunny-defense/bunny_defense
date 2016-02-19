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
  val iter = src.getLines().filter( _ != "" ).map(_.split(","))
  var spawn_scheduler = new Queue[(Double,Bunny)]
  val test_path = new Path
  test_path += bunnystart
  test_path += bunnyend
  val test_progress = new Progress(test_path)

  val law = new Random()

  val mappage: Map[String, Bunny] = Map(
    "Bunny"       -> (new Bunny(NormalBunny,test_progress)),
    "HeavyBunny"  -> (new Bunny(HeavyBunny, test_progress)),
    "Hare"        -> (new Bunny(Hare,       test_progress)),
    "Otter"       -> (new Bunny(Otter,      test_progress)),
    "GoldenBunny" -> (new Bunny(GoldenBunny,test_progress))
  )
  def create(): Queue[(Double,Bunny)] = {
    for (appear <- iter) {
      if (law.nextDouble > 1/1000) {
        val class_name = appear(1).trim
        spawn_scheduler += (( appear(0).toDouble, mappage(class_name) ))
      }
      else {
        spawn_scheduler += ((appear(0).toDouble, mappage("Golden_Bunny")))
      }
    }
    return(spawn_scheduler)
  }

}
