
package gui

import util.Random

import runtime.Controller

/* This animations adds thunder flashes to the rain animation */

object ThunderstormAnimation
{
    val rng = new Random
}

class ThunderstormAnimation(duration: Double) extends RainAnimation(duration)
{
    import ThunderstormAnimation._
    override def update(dt: Double): Unit = {
        super.update(dt)
        if( rng.nextDouble < (dt / 10) )
            Controller += new ThunderflashAnimation
    }
}
