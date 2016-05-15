package strategy

import runtime.TowerDefense
import game_mechanics._
import game_mechanics.bunny._
import game_mechanics.tower._
import game_mechanics.utilitary._
import tcp._
import gui._
import gui.animations._
import swing._

import java.net.Socket
import util.Random

/* Only the father class of the strategies. However, it needs to own the same
 * classes and attributes than its childrens, in order to have be valid statically
 */
class Strategy {

    class DisplayStrategy {
       val paint = {}
       val next    = {}
       def rain(dt: Double) = {}
       def scroll(dt: Double) : Unit = {}
    }
    class UpdateStrategy {
       val animation = {}
       val updatable = {}
       val utilitaries = {}
       val projectiles = {}
       def on_death(bunny : Bunny) = {}
       def update_timer(dt:Double) : Unit = {}
    }
    class ConnStrategy {
        def open_conn() = {}
    }


    val displaystrategy = new DisplayStrategy()
    val updatestrategy  = new UpdateStrategy()
    val connstrategy    = new ConnStrategy()
}

class ServerStrategy extends Strategy {
    /** The Server strategy class. It owns classes as attributes, in order to
     *  call the strategy in a straight forward way.
     */
    class DisplayStrategy {
        /* The Server displays anything */
       val paint = {}
       val next    = { StateManager.set_state(Lobby)}
       def rain(dt: Double) = {}
       def scroll(dt: Double) : Unit = {}
    }

    class UpdateStrategy {
       val animation = {}
       val updatable = {}
       val utilitaries = {}
       val projectiles = {}
       def on_death(bunny : Bunny) = {}
       def update_timer(dt:Double) : Unit = {
           TowerDefense.gamestate.timer -= dt
           if (TowerDefense.gamestate.timer <= 0) {
               TowerDefense.gamestate.timer = TowerDefense.gamestate.timer_init
               ServerThread.sync()
           }
       }
    }

    class ConnStrategy {
        def open_conn() = {}
    }


    val displaystrategy = new DisplayStrategy()
    val updatestrategy  = new UpdateStrategy()
    val connstrategy    = new ConnStrategy()
}

class ClientStrategy extends Strategy {
    /** The Client strategy class. It owns classes as attributes, in order to
     *  call the strategy in a straight forward way.
     */
    class DisplayStrategy {
       val rng =  new Random
       val paint = { TowerDefense.mainpanel.repaint()}
       val next    = {
           Dialog.showMessage( TowerDefense.map_panel, "Game Over")
           StateManager.set_state(Lobby)
       }
       def rain(dt: Double) = {
           if (rng.nextDouble < (dt / 200) && !TowerDefense.gamestate.raining )
           {
               TowerDefense.gamestate.raining = true
               val time    = 30 + rng.nextDouble * 120
               val (anim, to_send)  = if (rnf.nextDouble < 0.5)
                    (new ThunderstormAnimation(time),
                    (true , time ))
                else
                    (new RainAnimation(time),
                    (false, time ))
               anim and_then { () => this.raining = false }
               this += anim
               ServerThread.add(to_send)
           }
       }
       def scroll(dt:Double) : Unit = { TowerDefense.gamestate.scroll(dt)}
    }

    class UpdateStrategy {
       val animation   = {}
       val updatable   = {}
       val utilitaries = {}
       val projectiles = {}
       def on_death(bunny : Bunny) = {
           bunny.on_death()
           TowerDefense.gamestate += new GoldAnimation(
               bunny.reward(TowerDefense.gamestate.wave_counter),
               bunny.pos.clone()
           )
           Player.add_gold( bunny.reward(TowerDefense.gamestate.wave_counter))
           TowerDefense.gamestate -= bunny
           Player.killcount += 1
       }
    }

    class ConnStrategy {
        def open_conn(domain :String) : ClientThread = {
            return new ClientThread(domain)
        }
    }

    val displaystrategy = new DisplayStrategy()
    val connstrategy    = new ConnStrategy()
    val updatestrategy  = new UpdateStrategy()
}

// vim: set ts=4 sw=4 et:
