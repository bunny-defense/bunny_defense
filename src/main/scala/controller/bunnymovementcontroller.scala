package controller

import game_mechanics.bunny.Bunny
import game_mechanics.path.Progress
import utils.Continuable

/** A BunnyMovementController is a controller that manages how bunnies move
 */
trait BunnyMovementController
extends Continuable
{
    /** Moves the bunny
     @param bunny to move
     @param dt delta time since last update
     */
    def move(bunny: Bunny, dt: Double) : Unit
}

/** The IdleBunnyMovementController is a controller that makes a bunny idle,
 that is, stay still
 */
class IdleBunnyMovementController
extends BunnyMovementController
{
    override def move(bunny: Bunny, dt: Double) : Unit = {}
}

/** The PathBunnyMovementController is a controller that makes a bunny follow
 a path.
 @param path that the bunny will follow
 */
class PathBunnyMovementController(path: Progress)
extends BunnyMovementController
{
    override def move(bunny: Bunny, dt: Double) : Unit = {
        path.move(dt * bunny.speed)
        bunny.pos = path.get_position() + bunny.spread
        if(path.reached) //If at the end of the path, execute the continuation
            continue()
    }
}
