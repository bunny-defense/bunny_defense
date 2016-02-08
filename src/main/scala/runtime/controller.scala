
package runtime

object Controller
{
  val bunnies = new ListBuffer[Bunny]

  def update(dt: Double): Unit = {
    for( bunny <- bunnies )
      bunny.update(dt)
  }
}
