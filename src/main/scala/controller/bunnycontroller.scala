package controller

import game_mechanics.bunny.Bunny
import manager.BunnyManager

/** A BunnyController updates a single bunny
 */
trait BunnyController
{
    def update(bunny: Bunny, dt: Double) : Unit
}

/** The simple bunny controller is a controller that makes the bunny behave
 as a regular enemy, without special behavior or ability */
class SimpleBunnyController(
    bunnyManager: BunnyManager)
//  AnimationManager, WaveManager)
extends BunnyController
{
    //TODO make commented text uncommentable modulo some modifications
    override def update(bunny: Bunny, dt: Double) : Unit = {
        if(!bunny.alive)
        {
            bunny.on_death()
            //this.bunny_death_render_strategy(bunny)
            val damager = bunny.last_damager.get // If bunny crashes, it is a bug
            //damager.add_gold(bunny.reward(this.wave_counter))
            damager.killcount += 1
            bunnyManager.removeBunny(bunny)
        }
        bunny.move(dt)
        /* This should be the continuation of the BunnyMovementController
        if(bunny.path.reached) {
            bunnyManager.removeBunny(bunny)
            this.bunny_reach_goal_strategy(bunny)
        }
        */
    }
}
