
package runtime

import path._

/* Creates an abstract class of bunnies. Then every species of bunny is a
subclass of this class */
abstract class Bunny {
  val hp = 10
  val x = 2.
  val y = 2.
  val speed = 1.
  val path = new Path
}

class Heavy_Bunny extends Bunny {
  val hp = 20
  val x = 2.
  val y = 2.
  val speed = 0.5
  val path = new Path
}
