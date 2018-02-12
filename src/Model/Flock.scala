package Model
import scala.swing.Graphics2D
import java.awt.geom.{Ellipse2D, Line2D}
import java.awt.Color

/** A class which represents a bunch of Boids.
  * 
  * Contains useful methods to handle a group of Boids and to get useful values
  * like average velocity and position.
  * 
  * @constructor create a new Flock with given Vector of Boids
  * @param boids the Vector of Boids
  * @author Esko-Kalervo Salaka
  */
class Flock(var boids: Vector[Boid]) {
  
  /** Adds a new Boid to the Flock.
    */
  def addBoid(boid: Boid): Unit = 
    this.boids = this.boids :+ boid
  
  /** Returns all the Boids in the vision of the given boid as a Vector.
    */
  def boidsInVision(boid: Boid): Flock = new Flock(this.boids.par.filter { 
    another => boid.sees(another) 
  }.toVector)
  
  /** Returns all the Boids within a given distance of a given position.
    */
  def boidsInRange(position: Position2D, distance: Double): Flock = 
    new Flock(this.boids.par.filter { 
    boid => boid.inRange(position, distance)
  }.toVector)
  
  /** Returns the closest Boid of a given position wrapped in Option and None if
    * there are no boids in the Flock
    */
  def closest(position: Position2D): Option[Boid] = {
    if (this.boids.length == 0) None
    else if (this.boids.length == 1) Option(this.boids.head)
    
    else {
      Option(this.boids.foldLeft(this.boids.head)((boid2, boid1) => 
        if (boid2.distance(position) < boid1.distance(position)) boid2
        else boid1
    ))}
  }
  
  def closestInDrawingRadius(position: Position2D): Option[Boid] = {
    val closest = this.closest(position)
    
    if (closest.isEmpty) None
    else if (closest.get.distance(position) <= closest.get.drawRadius) 
      Option(closest.get)
    else None
  }
  
  /** Returns the average position of all the Boids in the Flock. 
    */
  def avgPosition: Position2D = 
    Position2D.average(this.boids.map { _.position })
  
  /** Returns the average velocity of all the Boids in the Flock. 
    */
  def avgVelocity: Vector2D = 
    Vector2D.average(this.boids.map { _.velocity })
  
  /** Returns the average direction of all the Boids in the Flock. 
    */
  def avgDirection: Vector2D = 
    Vector2D.average(this.boids.map { _.direction })
    
  /** Sets the vision angle of all the Boids in the Flock o a given one.
    */
  def setVisionAngle(visionAngle: Double): Unit = 
    this.boids.foreach { _.visionAngle = visionAngle }
  
  /** Sets the repulsion distance of all the Boids in the Flock o a given one.
    */
  def setRepulsionDistance(repulsionDistance: Double): Unit = 
    this.boids.foreach { _.repulsionDistance = repulsionDistance }
  
  /** Sets the vision distance of all the Boids in the Flock o a given one.
    */
  def setVisionDistance(visionDistance: Double): Unit = 
    this.boids.foreach { _.visionDistance = visionDistance }
  
  /** Sets the maximum speed of all the Boids in the Flock o a given one.
    */
  def setMaxSpeed(maxSpeed: Double): Unit = 
    this.boids.foreach { _.maxSpeed = maxSpeed }
  
  /** Sets the maximum force of all the Boids in the Flock o a given one.
    */
  def setMaxForce(maxForce: Double): Unit = 
    this.boids.foreach { _.maxForce = maxForce }
  
  /** Sets the draw radius of all the Boids in the Flock o a given one.
    */
  def setDrawRadius(drawRadius: Double): Unit = 
    this.boids.foreach { _.drawRadius = drawRadius }
  
  /** Sets the draw color of all the Boids in the Flock o a given one.
    */
  def setDrawColor(drawColor: Color): Unit = 
    this.boids.foreach { _.drawColor = drawColor }
  
  /** Draws all the Boids using a given Graphics2D instance.
    */
  def drawBoids(g: Graphics2D): Unit = 
    this.boids.par.foreach{ _.draw(g.create().asInstanceOf[Graphics2D])}
  
  /** Removes all the Boids from this Flock.
    */
  def clear(): Unit = this.boids = Vector()

}

/** Factory for Flock instances. */
object Flock {
  
  /** Creates an empty Flock.
    */
  def apply(): Flock = new Flock(Vector())
  
  /** Creates a new Flock with given Vector of Boids.
    */
  def apply(boids: Vector[Boid]): Flock = new Flock(boids)
  
  /** Creates a new Flock with given Flock instance.
    */
  def apply(flock: Flock): Flock = new Flock(flock.boids)
  
}