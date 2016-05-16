
package gui.animations

import util.Random

import runtime.TowerDefense

/* This animations adds thunder flashes to the rain animation */

object ThunderstormAnimation
{
    val rng = new Random
}

class ThunderstormAnimation(gamestate: ClientGameState, duration: Double)
extends RainAnimation(gamestate, duration)
{
    import ThunderstormAnimation._
    override def update(dt: Double): Unit = {
        super.update(dt)
        if( rng.nextDouble < (dt / 10) )
            gamestate += new ThunderflashAnimation
    }
}
