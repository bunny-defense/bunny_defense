
package runtime

import swing._
import swing.event._

/** A state of a state machine

 This represents a state in the program's state machine
 (Managed by the StateManager)

 */
abstract class State extends Reactor
{
    def update(dt: Double) : Unit
    def render(g: Graphics2D) : Unit
    def on_event(event: Event) : Unit
}
