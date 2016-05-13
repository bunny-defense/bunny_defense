package strategy

import runtime.TowerDefense

/* Only the father class of the strategies. */
class Strategy {}

class ServerStrategy extends Strategy {
    /** The Server strategy class. It owns classes as attributes, in order to
     *  call the strategy in a straight forward way.
     */
    class DisplayStrategy {
        /* The Server displays anything */
       val repaint = {}
       val gameover = { return }
    }

    class ConnStrategy {
        val open_conn = {
            new Server()
        }
}

class ClientStrategy extends Strategy {
    /** The Client strategy class. It owns classes as attributes, in order to
     *  call the strategy in a straight forward way.
     */
    class DisplayStrategy {
       val repaint =  {TowerDefense.mainpanel.repaint()}
       val gameover = {
           Dialog.showMessage( TowerDefense.map_panel, "Game Over")
           return
       }
    }

    class ConnStrategy {
        val open_conn = {
            new Client()
        }
}

// vim: set ts=4 sw=4 et:
