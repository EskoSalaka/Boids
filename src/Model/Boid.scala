package Model
import math.abs
import scala.util.Random
import scala.swing.Graphics2D
import java.awt.Color
import java.awt.geom.{Ellipse2D, Line2D}

/** A class which represents a Boid or a bird-oid object.
  * 
  * It is basically considered a static object with certain characterist, like 
  * velocity and position, and with its methods it can be moved around.
  * 
  * @constructor create a new Boid with given characteristics
  * @param position the position of the Boid
  * @param velocity the velocity of Boid
  * @param visionAngle the vision angle of the Boid
  * @param repuslionDistance the repulsion distance of the Boid
  * @param visionDistance the vision distance of the Boid
  * @param maxSpeed the maximum speed of the Boid
  * @param maxForce the maximum force of the Boid
  * @param drawRadius the drawing radius of the Boid
  * @param drawColor the drawing color of the Boid
  * @author Esko-Kalervo Salaka
  */
class Boid(var position: Position2D, 
           var velocity: Vector2D,
           var visionAngle: Double,
           var repulsionDistance: Double,
           var visionDistance: Double,
           var maxSpeed: Double,
           var maxForce: Double,
           var drawRadius: Double = 5.0,
           var drawColor: Color = Color.BLACK) {
  
  var direction: Vector2D = this.velocity.normalized
  
  /** Returns the distance from this to another given Boid.
    */
  def distance(another: Boid): Double = this.position.distance(another.position)
  
  /** Returns the distance from this to an Obstacle.
    */
  def distance(obstacle: Obstacle): Double = 
    this.position.distance(obstacle.position)
  
  /** Returns the distance from this to a Position2D.
    */
  def distance(position: Position2D): Double = this.position.distance(position)
  
  /** Returns the angle between this and another given Boid.
    */
  def angle(another: Boid) = 
    this.direction.angle(Vector2D(this.position, another.position))
  
  /** Returns true if this can see another given Boid
    */
  def sees(another: Boid): Boolean = {
    this != another &&
    this.distance(another) <= this.visionDistance &&
    this.angle(another) <= this.visionAngle
  }
  
  /** Returns true if this is inside a given range from a given Position2D.
    */
  def inRange(position: Position2D, distance: Double): Boolean = 
    this.position.isInRange(position, distance)
  
  /** Moves this Boid forward inside given bounds accoring to it's velocity 
    * vector. This means that the x coordinate of the Boid can't be less than
    * 0 and more that xBound and same for it's y-coordinate. The bounds
    * represent screen width and height and if the Boid would cross those, it
    * would pop out of the other end.
    * 
    * @param xBound the x-axel bound length
    * @param yBound the y-axel bound length
    */  
  def moveInBounds(xBound: Double, yBound: Double): Unit = 
    this.position = this.position.movedInBounds(this.velocity, xBound, yBound)
  
  
  /** Applies a force vector to this Boids velocity.
    * 
    * Note, that the given force can be greater than this Boid's maximum force,
    * but the resulting new velocity is truncated to it's maximum velocity.
    */  
  def applyForce(force: Vector2D): Unit = {
    val newVel = this.velocity + force
    this.velocity = newVel.limited(this.maxSpeed)
    
    if (this.velocity.nonZero) this.direction = this.velocity.normalized
  }
  
  /** Draws this boid using a given Graphics2D object, it's drawing radius and
    * drawing color. 
    */
  def draw(g: Graphics2D): Unit = {
    g.setColor(this.drawColor)
    val shape1 = new Ellipse2D.Double(this.position.x - this.drawRadius, 
                                      this.position.y - this.drawRadius, 
                                      this.drawRadius * 2, this.drawRadius * 2)
  
    val shape2 = {
     val p2 = this.position.moved(this.velocity.scaled(this.drawRadius + 4))
     this.velocity.toJavaLine2D(p2)
     }
    
    g.fill(shape1)
    g.draw(shape2)
  }
}

/** Factory for Boid instances. */
object Boid {
  
  private var random = new Random(System.currentTimeMillis())
  
  private val colors = 
    Seq(Color.RED, Color.BLACK, Color.BLUE, Color.GRAY,Color.GREEN, 
        Color.YELLOW, Color.CYAN, Color.MAGENTA,Color.PINK, Color.DARK_GRAY, 
        Color.ORANGE)
  
  private def getRandomColor = 
   this.colors(this.random.nextInt(this.colors.length - 1))
  
 /** Creates a new Boid with a random color and a random position, which will be
   * inside given bounds. The boid will be moving in a random direction with 
   * it's maximum speed. The draw radius is set to 5.
   * 
   * @param xMin the minimum x-constraint for the position
   * @param yMin the minimum y-constraint for the position
   * @param xMax the maximum x-constraint for the position
   * @param yMax the maximum y-constraint for the position
   * @param visionAngle the vision angle of the Boid
   * @param repuslionDistance the repulsion distance of the Boid
   * @param visionDistance the vision distance of the Boid
   * @param maxSpeed the maximum speed of the Boid
   * @param maxForce the maximum force of the Boid
   */
 def getRandomPosBoid(xMin: Int, yMin: Int, 
                      xMax: Int, yMax: Int,
                      visionAngle: Double,
                      repulsionDistance: Double,
                      visionDistance: Double,
                      maxSpeed: Double,
                      maxForce: Double): Boid = {
   val randomPos = Position2D.getRandom(xMin, yMin, xMax, yMax)
   val randomDir = Vector2D.getRandomUnit()
   
   new Boid(randomPos, 
            randomDir * maxSpeed,
            visionAngle,
            repulsionDistance,
            visionDistance,
            maxSpeed,
            maxForce,
            5.0,
            this.getRandomColor)
 }
 
 /** Creates a new Boid with a random color. The boid will be moving in a 
   * random direction with it's maximum speed.
   *  
   * @param position the position of the Boid
   * @param visionAngle the vision angle of the Boid
   * @param repuslionDistance the repulsion distance of the Boid
   * @param visionDistance the vision distance of the Boid
   * @param maxSpeed the maximum speed of the Boid
   * @param maxForce the maximum force of the Boid
   */
 def getRandomBoid(position: Position2D,
                   visionAngle: Double,
                   repulsionDistance: Double,
                   visionDistance: Double,
                   maxSpeed: Double,
                   maxForce: Double): Boid = {
   val randomDir = Vector2D.getRandomUnit()
   
   new Boid(position, 
            randomDir * maxSpeed,
            visionAngle,
            repulsionDistance,
            visionDistance,
            maxSpeed,
            maxForce,
            5.0, 
            this.getRandomColor)
 }

  
}