
package gui

import util.Random

import runtime.Controller

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
