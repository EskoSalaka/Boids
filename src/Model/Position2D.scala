package Model

import scala.math.{sqrt, round, abs}
import scala.util.Random
import java.awt.geom.Point2D

/** An immutable two dimensional coordinate in the Cartesian coordinate system.
  * Has Double precision
  * 
  * Typically, can be used to represent the position of some object on the 
  * screen.
  * 
  * @constructor create a new Position2D with given x and y coordinates
  * @param x the x coordinate
  * @param y the y coordinate
  * @author Esko-Kalervo Salaka
  */
class Position2D(val x: Double, val y: Double) {
  
  /** Returns the distance between this and another position.
    */
  def distance(another: Position2D): Double = {
    val xDist = another.x - this.x
    val yDist = another.y - this.y
    
    sqrt(xDist * xDist + yDist * yDist)
  }
  
  /** Returns a new position with this position moved with a given velocity 
    * vector.
    * 
    * Basically, returns a new position from the other end of the vector having
    * this position as the staring point.
    *
    * @param velocity a velocity vector to move the position
    * @return a new position moved with a given velocity vector
    */
  def moved(velocity: Vector2D): Position2D = 
    Position2D(this.x + velocity.x, this.y + velocity.y)
    
  /** Returns a new position within bounds with this position moved with a 
    * given velocity vector.
    * 
    * If this new position would be out of given bounds it will be moved
    * to the other side of the bounds. On screen if the bounds would be screen
    * height and width it would look like the object pops out from the other
    * side of the screen.
    *
    * @param velocity a a vector representing velocity
    * @param xBound a a vector representing velocity
    * @param yBound a a vector representing velocity
    * @return a new position moved with a given velocity vector
    */
  def movedInBounds(velocity: Vector2D, 
                    xBound: Double, 
                    yBound: Double): Position2D = {
    val moved = this.moved(velocity)
    
    val x = {
      if (moved.x > xBound || moved.x < 0.0) xBound - abs(moved.x)
      else moved.x
    }
    
    val y = {
      if (moved.y > yBound || moved.y < 0.0) yBound - abs(moved.y)
      else moved.y
    }
    
    Position2D(x, y)
  }
  
  /** Returns true if the distance to another point is less or equal than given 
    * distance and false otherwise
    */
  def isInRange(another: Position2D, distance: Double): Boolean = 
    this.distance(another) <= distance
    
  /** Returns a new equivilant Java Point2D.Double object. 
    */
  def toJavaPoint2D = new Point2D.Double(this.x, this.y)
  
  
}

/** Factory for Position2D instances. */
object Position2D {
  
  private var random = new Random(System.currentTimeMillis())
  
  /** Creates a new default Position2D with coordinates (0, 0)
    */
  def apply():Position2D = new Position2D(0.0D, 0.0D)
  
  /** Creates a new Position2D with a given Position2D instance. Basically a
    * copy.
    */
  def apply(another: Position2D): Position2D = 
    new Position2D(another.x, another.y)
  
  /** Creates a new Position2D with given Integer coordinates.
    */
  def apply(x: Int, y: Int): Position2D = 
    new Position2D(x.toDouble, y.toDouble)
  
  /** Creates a new Position2D with given Double precision coordinates.
    */
  def apply(x: Double, y: Double): Position2D = 
    new Position2D(x, y)
  
  /** Returns a random  Position2D with coordinates inside a given range.
    */
  def getRandom(xMin: Int, yMin: Int, 
                xMax: Int, yMax: Int): Position2D = {
    val xRange = xMax - xMin
    val yRange = yMax - yMin
    
    Position2D(xMin + random.nextInt(xRange + 1),
               yMin + random.nextInt(yRange + 1))
  }
  
  /** Returns a random  Position2D with coordinates inside a given range.
    */
  def getRandom(xMin: Double, yMin: Double, 
                xMax: Double, yMax: Double): Position2D = {
    val xRange = xMax - xMin
    val yRange = yMax - yMin
    
    Position2D(xMin + random.nextDouble() * xRange,
               yMin + random.nextDouble() * yRange)
  }
  
  /** Returns the average Position2D of a sequence on Position2D instances.
    */
  def average(positions: Seq[Position2D]): Position2D = {
    val length = positions.length
    
    if (length == 0) Position2D()
    else{
      new Position2D(positions.map { _.x }.sum / length, 
                     positions.map { _.y }.sum / length)
    }
  }
}