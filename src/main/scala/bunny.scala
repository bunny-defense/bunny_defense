
package runtime

import path._

/* Creates an abstract class of bunnies. Then every species of bunny is a
subclass of this class */
abstract class Bunny {
  var hp = 10
  var x = 2.
  var y = 2.
  var shield = 1.
  var speed = 1.
  var path = new Path
}

class Heavy_Bunny extends Bunny {
  this.hp = 20
  this.shield = 1.5
  this.speed = 0.5
}
