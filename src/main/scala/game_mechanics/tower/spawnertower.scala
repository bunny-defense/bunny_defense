
package game_mechanics.tower

import collection.mutable.ListBuffer

import game_mechanics._
import game_mechanics.path._
import runtime.{Spawner,Controller,TowerDefense}
import game_mechanics.bunny._
import game_mechanics.JPS
import util.Random

class SpawnerTower() extends TowerType
{
    /**
     * The class that defines the methods of all spawners
      */
    val law = new Random()
    /* The following are stats modifiers that apply to spawned bunnies */
    override def attack_from(tower : Tower): () => Boolean = {
        def get_right_type(): Boolean = {
            var new_bunny = BunnyFactory.create(tower.bunnies_spawning.head,0)
            new_bunny.base_speed = new_bunny.base_speed * tower.speed_modifier
            new_bunny.hp = new_bunny.initial_hp * tower.health_modifier
            new_bunny.initial_hp = new_bunny.initial_hp * tower.health_modifier
            new_bunny.path = new Progress(
                new JPS(tower.pos,
                    new CellPos(TowerDefense.map_panel.map.width,
                        law.nextInt(TowerDefense.map_panel.map.height/2)+
                            TowerDefense.map_panel.map.height/4
                    )
                ).run()
                    match {
                    case None    => throw new Exception()
                    case Some(p) => p
                }
            )
            Controller += new_bunny
            tower.bunnies_spawning = tower.bunnies_spawning.tail ::: List(tower.bunnies_spawning.head)
            return true
        }
        return get_right_type
    }
}

