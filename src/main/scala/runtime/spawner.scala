package runtime

import collection.mutable.Queue
import collection.immutable.Map
import util.Random
import util.control.Exception

import game_mechanics.bunny.BunnyFactory
import game_mechanics.path._

object Spawner
{
    val law        = new Random()
    val bunnystart = new CellPos( -1, TowerDefense.gamestate.map_panel.map.height/2)
    val bunnyend   = new CellPos( TowerDefense.gamestate.map_panel.map.width,
                                  TowerDefense.gamestate.map_panel.map.height/2)
}

class Spawner(id: Int) {
    import Spawner._
    val src = scala.io.Source.fromFile("src/main/resources/waves/wave"+id.toString+".csv")
    val iter = src.getLines().filter( _ != "" ).map(_.split(","))
    var spawn_scheduler = new Queue[(Double,Int)]
    var has_boss = false

    val law = new Random()

    val mappage: Map[String, Int] = Map(
        "Bunny"          -> BunnyFactory.NORMAL_BUNNY,
        "HeavyBunny"     -> BunnyFactory.HEAVY_BUNNY,
        "Hare"           -> BunnyFactory.HARE_,
        "Otter"          -> BunnyFactory.OTTER_,
        "GoldenBunny"    -> BunnyFactory.GOLDEN_BUNNY,
        "BadassBunny"    -> BunnyFactory.BADASS_BUNNY,
        "SpecOpBunny"    -> BunnyFactory.SPECOP_BUNNY,
        "FlyingSquirrel" -> BunnyFactory.FLYING_SQUIRREL,
        "ShieldBunny"    -> BunnyFactory.SHIELD_BUNNY
    )

    def create(): Queue[(Double,Int)] = {
        for (appear <- iter) {
            if (law.nextDouble > 1.0/1000.0) {
                val class_name = appear(1).trim
                if( class_name == "Otter" )
                    has_boss = true
                spawn_scheduler += (( appear(0).toDouble, mappage(class_name) ))
            }
            else {
                spawn_scheduler += (( appear(0).toDouble, BunnyFactory.GOLDEN_BUNNY))
            }
        }
        return(spawn_scheduler)
    }

}
