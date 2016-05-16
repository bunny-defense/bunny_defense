
package gui.animations

import java.awt.Graphics2D

import runtime.TowerDefense
import gui._
import utils.Continuable


abstract class Animatable(gamestate: ClientGameState)
extends Continuable
{
    /** Animation superclass */
    var timer     = 1.0
    val map_panel = gamestate.map_panel

    def on_timer_ran_out(): Unit = {
        gamestate -= this
        continue()
    }

    def update(dt: Double): Unit = {
        timer -= dt
        if( timer <= 0 )
            on_timer_ran_out()
    }

    def draw(g: Graphics2D): Unit
}

