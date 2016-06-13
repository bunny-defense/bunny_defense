
package gui.animations

import util.Random

import runtime.TowerDefense
import runtime.GuiGameState

/* This animations adds thunder flashes to the rain animation */

object ThunderstormAnimation
{
    val rng = new Random
}

class ThunderstormAnimation(duration: Double, gamestate: GuiGameState)
extends RainAnimation(duration, gamestate)
{
    import ThunderstormAnimation._
    override def update(dt: Double): Unit = {
        super.update(dt)
        if( rng.nextDouble < (dt / 10) )
            gamestate += new ThunderflashAnimation(gamestate)
    }
}
