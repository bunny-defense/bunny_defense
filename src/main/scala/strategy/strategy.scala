package strategy

import runtime.TowerDefense
import game_mechanics._
import tcp._
import gui._
import swing._

import java.net.Socket

/* Only the father class of the strategies. */
class Strategy {}

class ServerStrategy extends Strategy {
    /** The Server strategy class. It owns classes as attributes, in order to
     *  call the strategy in a straight forward way.
     */
    class DisplayStrategy {
        /* The Server displays anything */
       val repaint = {}
    }

    class ConnStrategy {
        def open_conn() = {}
    }


    val displaystrategy = new DisplayStrategy()
    val connstrategy    = new ConnStrategy()
}

class ClientStrategy extends Strategy {
    /** The Client strategy class. It owns classes as attributes, in order to
     *  call the strategy in a straight forward way.
     */
    class DisplayStrategy {
       val repaint =  {TowerDefense.mainpanel.repaint()}
    }

    class ConnStrategy {
        def open_conn(domain :String) : Client = {
            return new Client(domain)
        }
    }

    val displaystrategy = new DisplayStrategy()
    val connstrategy    = new ConnStrategy()
}

// vim: set ts=4 sw=4 et:
