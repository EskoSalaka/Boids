package Model

import scala.math.{sqrt, acos, toDegrees}
import scala.util.Random

import java.awt.geom.Line2D

/** An immutable two dimensional Euclidean vector, that can be used with the   
  * Cartesian coordinate system. Has Double precision.
  * 
  * Note, that this shouldn't be confused with the Scala Vectors! 
  * 
  * @constructor create a new Vector2D with given x and y components
  * @param x the x-component
  * @param y the y-component
  * @author Esko-Kalervo Salaka
  */
class Vector2D (val x: Double, val y: Double)  {
  
  /** Returns true if this vector is non-zero and false otherwise
    */
  def nonZero: Boolean = this.x != 0.0 || this.y != 0.0
  
  /** Returns The length of this vector squared. 
    */
  def lengthSquared: Double = this.x * this.x + this.y * this.y
  
  /** Returns The length of this vector.
    */
  def length: Double = sqrt(this.lengthSquared)
  
  /** Returns a new vector as a vector sum of this and another vector.
    */
  def addition(another: Vector2D): Vector2D = 
    Vector2D(this.x + another.x, this.y + another.y)
  
  /** Returns a new vector as a vector sum of this a sequence of other vectors.
    */
  def addition(vectors: Seq[Vector2D]): Vector2D = 
    Vector2D(vectors.map { _.x }.sum + this.x, vectors.map { _.y }.sum + this.y)
  
  /** Returns a new vector as a scalar product with this and another vector.
    */
  def scalarProduct(scalar: Double): Vector2D = 
    Vector2D(this.x * scalar, this.y * scalar)
  
  /** Returns a new vector as a vector product with this and another vector.
    */
  def vectorProduct(another: Vector2D): Double = 
    this.x * another.x + this.y * another.y
  
  /** Returns the angle in degree between this and another vector in degrees.
    */
  def angle(another: Vector2D): Double = 
    acos(this.vectorProduct(another) / (this.length * another.length)).toDegrees
  
    

  /** Returns a new normalized version of this vector.
    */
  def normalized: Vector2D = {
    if (this.length != 0.0) {
      this.scalarProduct(1.0 / this.length)
    } else {
      Vector2D(this)
    }
  }
  
  /** Returns a new vector having an exact opposite direction as this.
    */
  def reversed: Vector2D = Vector2D(-this.x, -this.y)
  
  /** Returns a new vector with same direction but length scaled to given 
    * scalar.
    */
  def scaled(scalar: Double): Vector2D = this.normalized.scalarProduct(scalar)
  
  /** Returns a new vector with same direction but length limited to the length
    * of a given vector.
    */
  def limited(another: Vector2D): Vector2D = {
    if (this.length > another.length) {
      this.scaled(another.length)
    } else {
      Vector2D(this)
    }
  }
  
  /** Returns a new vector with same direction but length limited to the length
    * of a given scalar value.
    */
  def limited(scalar: Double): Vector2D = {
    if (this.length > scalar) {
      this.scaled(scalar)
    } else {
      Vector2D(this)
    }
  }
  
  /** Returns a new Java Line2D version of this vector having a given staring
    * position.
    */
  def toJavaLine2D(startingPoint: Position2D): Line2D.Double = {
    val movedPoint = startingPoint.moved(this)
    new Line2D.Double(startingPoint.x, startingPoint.y, 
                      movedPoint.x, movedPoint.y)
  }
    
  
  def +(another: Vector2D): Vector2D = this.addition(another)
  
  def +(vectors: Seq[Vector2D]): Vector2D = this.addition(vectors)
    
  def -(another: Vector2D): Vector2D = this.addition(another.reversed)
  
  def -(vectors: Seq[Vector2D]): Vector2D = 
    this.addition(vectors.map { _.reversed })
  
  def *(another: Vector2D): Double = this.vectorProduct(another)
  
  def *(scalar: Double): Vector2D = this.scalarProduct(scalar)
  
  def /(scalar: Double): Vector2D = this.scalarProduct(1.0 / scalar)
}

/** Factory for Vector2D instances. */
object Vector2D{
  private var random = new Random(System.currentTimeMillis())

  /** Creates a new default Vector2D with 0 length. A Zero vector.
    */
  def apply(): Vector2D = {
    new Vector2D(0.0, 0.0)
  }
  
  /** Creates a new Vector2D with a given Vector2D instance. Basically a
    * copy.
    */
  def apply(another: Vector2D): Vector2D = {
    new Vector2D(another.x, another.y)
  }

  /** Creates a new Vector2D with a given x and y Double precision lengths.
    */
  def apply(x: Double, y: Double): Vector2D = {
    new Vector2D(x, y)
  }
  
  /** Creates a new Vector2D with a given x and y Integer precision lengths.
    */
  def apply(x: Int, y: Int): Vector2D = {
    new Vector2D(x.toDouble, y.toDouble)
  }
  
  /** Creates a new Vector2D from 2 given Position2D instances. A vector drawn
    * from position1 to position2 
    */
  def apply(position1: Position2D, position2: Position2D): Vector2D = 
    new Vector2D(position2.x - position1.x, position2.y - position1.y)
  
  /** Returns a random  Vector2D with it's length bounded inside a given bound.
    */
  def getRandom(xMin: Double, yMin: Double, 
                xMax: Double, yMax: Double): Vector2D = {
    val xRange = xMax - xMin
    val yRange = yMax - yMin
    
    Vector2D(xMin + random.nextDouble() * xRange,
             yMin + random.nextDouble() * yRange)
  }
  
  /** Returns a random  Vector2D with it's length bounded inside a given bound.
    */
  def getRandom(xMin: Int, yMin: Int, 
                xMax: Int, yMax: Int): Vector2D = {
    val xRange = xMax - xMin
    val yRange = yMax - yMin
    
    Vector2D(xMin + random.nextInt(xRange + 1),
             yMin + random.nextInt(yRange + 1))
  }
  
  /** Returns a random Vector2D with a length of 1. A random unit vector.
    *  
    * It's not as simple as it seems at first. To get a truly pseudorandom 
    * unit vector we first have to get a vector with a length less than 1. Then, 
    * if the length is less than 1 return it normalized.
    */
  def getRandomUnit(): Vector2D = {
    var notFound = true
    var x = 0.0
    var y = 0.0
    
    while(notFound){
      x = random.nextDouble() * 2.0 - 1.0
      y = random.nextDouble() * 2.0 - 1.0
     
      if (x*x + y*y < 1) notFound = false 
    }
    
    Vector2D(x, y).normalized
  }
  
  /** Returns an average Vector2D with given Seq of Vector2D's. Returns a zero
    * vector in case the Seq is empty.
    */
  def average(vectors: Seq[Vector2D]): Vector2D = {
    val length = vectors.length
    
    if (length == 0) Vector2D()
    else {
      new Vector2D(vectors.map { _.x }.sum / length, 
                   vectors.map { _.y }.sum / length)
    }
    
  }
  

}