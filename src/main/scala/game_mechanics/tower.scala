
package game_mechanics

import path._ // Really necessary ?

/* Abstract tower superclass from which evey tower will be derived */
abstract class Tower {
  var x = 2
  var y = 2
  val size = 1
  val damages = 5
  val radius = 1
  val sell_cost = 10
  val buy_cost = 10
 }
