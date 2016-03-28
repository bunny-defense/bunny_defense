package runtime

import collection.mutable.Queue
import collection.immutable.Map
import util.Random

import game_mechanics._
import game_mechanics.path._
import game_mechanics.tower._
import runtime._

object Spawner
{
  val bunnystart = new CellPos(
      -1,
      TowerDefense.map_panel.map.height / 2)
  val bunnyend   = new CellPos(
      TowerDefense.map_panel.map.width,
      TowerDefense.map_panel.map.height / 2)
  (new JPS( bunnystart, bunnyend )).run() match {
      case None => throw Exception
      case Some(p) => val path = p
  }
}

class Spawner(id: Int) {
  import Spawner._
  val src = scala.io.Source.fromFile("src/main/resources/waves/wave"+id.toString+".csv")
  val iter = src.getLines().filter( _ != "" ).map(_.split(","))
  var spawn_scheduler = new Queue[(Double,BunnyType)]
  var has_boss = false

  val law = new Random()

  val mappage: Map[String, BunnyType] = Map(
    "Bunny"       -> NormalBunny,
    "HeavyBunny"  -> HeavyBunny,
    "Hare"        -> Hare,
    "Otter"       -> Otter,
    "GoldenBunny" -> GoldenBunny,
    "BadassBunny" -> BadassBunny
  )
  def create(): Queue[(Double,BunnyType)] = {
    for (appear <- iter) {
      if (law.nextDouble > 1.0/1000.0) {
        val class_name = appear(1).trim
        if( class_name == "Otter" )
            has_boss = true
        spawn_scheduler += (( appear(0).toDouble, mappage(class_name) ))
      }
      else {
        spawn_scheduler += (( appear(0).toDouble, GoldenBunny ))
      }
    }
    return(spawn_scheduler)
  }

}
