
package game_mechanics.path

import Math._

/* 2D Point (int) */
class CellPos(x0: Int, y0: Int) {
  var x = x0
  var y = y0

  def ==(other: CellPos): Boolean = {
    return x == other.x && y == other.y
  }

  def !=(other: CellPos): Boolean = {
      return x != other.x || y != other.y
  }

  def <(other: CellPos): Boolean = {
      return (this.x < other.x || (this.x == other.x && this.y < other.y))
  }

  def +(other: CellPos): CellPos = {
    return new CellPos( x + other.x, y + other.y )
  }
  def -(other: CellPos): CellPos = {
    return new CellPos( x - other.x, y - other.y )
  }
  def +=(other: CellPos): CellPos = {
    x += other.x
    y += other.y
    return this
  }
  def *(scalar: Double): CellPos = {
    return new CellPos( (x * scalar).toInt, (y * scalar).toInt )
  }
  def /(scalar: Double): CellPos = {
    return new CellPos( (x / scalar).toInt, (y / scalar).toInt )
  }
  def &(vect: CellPos) : Double = {
    return vect.x * x + vect.y * y
  }


  def norm() : Double = {
    return Math.sqrt(Math.pow(this.x,2.0)+Math.pow(this.y,2.0))
  }

  def normalize() : Waypoint = {
    val n = norm
    return new Waypoint( x / n, y / n )
  }

  def distance_to( other: CellPos ): Double = {
    return math.sqrt(
      math.pow( x - other.x, 2 )
        + math.pow( y - other.y, 2 ) )
  }

  override def toString(): String = {
    return "(" + x.toString + "," + y.toString + ")"
  }

  def toDouble(): Waypoint = {
    return new Waypoint( x.toDouble, y.toDouble )
  }

  override def clone(): CellPos = {
    return new CellPos( x, y )
  }
}
