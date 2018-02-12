package Model
import scala.swing.Graphics2D

/** A base abstact class for flocking simulators. 
  *  
  *  It has methods to control a bunch of Boids and Obstacles, and also can 
  *  track mouse location.
  *  
  * @constructor create a new Position2D with given sequences of Boids and 
  * Obstacles and bounds
  * @param _boids Vector of Boid instances
  * @param obstacles Vector of Obstacle instances
  * @param xBound the x-length at which the Boids are bound inside
  * @param yBound the y-length at which the Boids are bound inside
  * @author Esko-Kalervo Salaka
  */
abstract class FlockingSimulator(_boids: Vector[Boid], 
                                 var obstacles: Vector[Obstacle],
                                 var xBound: Int, 
                                 var yBound: Int) extends Flock(_boids) {
  
  /** Moves all the Boids inside this simulations bounds.
    */
  protected def moveAll: Unit = this.boids.foreach { 
    _.moveInBounds(this.xBound, this.yBound) 
    }

  //These two vars determine if the mouse is attractive or repulsive
  var attractiveMouse: Boolean = false
  var repulsiveMouse: Boolean = false 
  
  var mouseLocation: Position2D = Position2D()
  
  /** Removes all the Boids and Obstacles contained in this class.
    */
  override def clear: Unit ={
    super.clear()
    this.obstacles = Vector()
  }
  
  /** Adds a given Obstacle to the Simulation.
  */
  def addObstacle(obstacle: Obstacle): Unit =
    this.obstacles = this.obstacles :+ obstacle
  
  /** Draws all Obstacles with the given Graphics2D instance.
  */
  def drawObstacles(g: Graphics2D): Unit = 
    this.obstacles.par.foreach{ _.draw(g) }
  
  /** This is the abstract method which all the implementing classes have to 
    * implement. 
    * 
    * Basically, at each frame calculates all necessairy forces and other 
    * interactions, applies forces and moves the boids according to their 
    * velocities. How it's done is up to implememntation. 
    */
  def updateAll: Unit
  
}

/** Our only simple simulation implementation.
  *  
  *  
  *  
  * @constructor create a new SimpleSim with given Boids, Obstacle, Bounds and
  * other characteristics
  * @param _boids Vector of Boid instances
  * @param _obstacles Vector of Obstacle instances
  * @param _xBound the x-length at which the Boids are bound inside
  * @param _yBound the y-length at which the Boids are bound inside
  * @param cohesionFactor the strength of cohesion
  * @param separationFactor the strength of separation
  * @param alignementFactor the strength of alignement
  * @param mouseAttractionRange the range of mouse attraction/repulsion
  * @param mouseAttractionFactor the strength of mouse attraction/repulsion
  * @param obstacleRepulsionFactor the strength of obstacle repulsion
  * @param obstacleRepulsionDistance the range of of obstacle repulsion
  * @author Esko-Kalervo Salaka
  */
class SimpleSim(_boids: Vector[Boid], 
                _obstacles: Vector[Obstacle],
                _xBound: Int, 
                _yBound: Int,
                var cohesionFactor: Double,
                var separationFactor: Double,
                var alignementFactor: Double,
                var mouseAttractionRange: Double,
                var mouseAttractionFactor: Double,
                var obstacleRepulsionFactor: Double,
                var obstacleRepulsionDistance: Double) extends 
                FlockingSimulator(_boids, _obstacles, _xBound, _yBound){

  
 /** Returns the separation steering force, or more like the direction of the 
   * force for a given Boid and its neighbours.
   * 
   * For each neighbour of the Boid a normalized repulsive force is calculated.
   * This force is basically a unit vector pointing towards this boid from the
   * neighbour. Then, a weight factor (1.0 - distance /boid.repulsionDistance)
   * is applied to the normalized vector. The resultant is the average of all 
   * these vectors. This resultant is used as a target of steering and returned
   * as normalized.
   * 
   * Note, that it is assumed that the length of neighbours is grater than 0.
   * 
   * @param boid the Boid to calculate the separation steering force for
   * @param neighbours the neighbours of the Boid
   * @return the separation steering force direction as a Vector2D
   */
  private def separate(boid: Boid, neighbours: Flock): Vector2D = {
    var sepSteer = Vector2D()
    
    neighbours.boids.par.foreach { neighbourBoid =>  
      val distance = boid.distance(neighbourBoid)
        
      if (distance < boid.repulsionDistance & distance > 0.0) {
        var force = Vector2D(neighbourBoid.position, boid.position).normalized
        force = force * (1.0 - distance /boid.repulsionDistance )
        sepSteer = sepSteer + force
      }
    }
      
    if (sepSteer.length != 0.0){
      sepSteer = sepSteer * (1.0 / neighbours.boids.length)
      sepSteer = sepSteer.scaled(boid.maxSpeed)
      sepSteer = sepSteer - boid.velocity
      }
    
    sepSteer.normalized
  }
  
 /** Returns the alignement steering force, or more like the direction of the 
   * force for a given Boid and its neighbours.
   * 
   * Simply, the average direction of this Boids neighbours scaled to this Boids 
   * max speed is used as a target of steering and returned normalized.
   * 
   * Note, that it is assumed that the length of neighbours is grater than 0.
   * 
   * @param boid the Boid to calculate the alignement steering force for
   * @param neighbours the neighbours of the Boid
   * @return the alignement steering force direction as a Vector2D
   */
  private def align(boid: Boid, neighbours: Flock): Vector2D = {
    var alignSteer = neighbours.avgDirection.scaled(boid.maxSpeed)
    alignSteer = alignSteer - boid.velocity
    alignSteer.normalized
  }
  
 /** Returns the cohesion steering force, or more like the direction of the 
   * force for a given Boid and its neighbours.
   * 
   * Simply, the average position of this Boids neighbours scaled to this Boids 
   * max speed is used as a target of steering and returned normalized.
   * 
   * Note, that it is assumed that the length of neighbours is grater than 0.
   * 
   * @param boid the Boid to calculate the alignement steering force for
   * @param neighbours the neighbours of the Boid
   * @return the alignement steering force direction as a Vector2D
   */
  private def cohesion(boid: Boid, neighbours: Flock): Vector2D = 
    this.seekPos(boid, neighbours.avgPosition).normalized
  
 /** Returns the seek steering force for a given Boid and a target position.
   * 
   * The given position is used as a target of steering in the Normal Reynolds
   * way. It's not returned normalized.
   * 
   * 
   * @param boid the Boid to calculate the seek steering force for
   * @param neighbours the neighbours of the Boid
   * @return the seek steering force direction as a Vector2D
   */
  private def seekPos(boid: Boid, position: Position2D): Vector2D = {
    var seekSteer = Vector2D(boid.position, position)
    seekSteer = seekSteer.scaled(boid.maxSpeed)
    seekSteer = seekSteer - boid.velocity
    seekSteer
  }
  
 /** Returns the exact opposite of seek steering force.
   */
  private def avoidPos(boid: Boid, position: Position2D): Vector2D = 
    this.seekPos(boid, position).reversed
  
 /** Returns the obstacle avoidance steering force for a given Boid and it's
   * close Obstacles.
   * 
   * The Obstacles can be viewed as emitting a force field from which the boids
   * want to steer away from.
   * 
   * @param boid the Boid to calculate the obstacle avoidance force for
   * @param closeObstacles the close Obstacles of the Boid
   * @return the obstacle avoidance force direction as a Vector2D
   */
  private def avoidObstacle(boid: Boid, 
                            closeObstacles: Vector[Obstacle]): Vector2D = {
    var obstacleAvoidSteer = Vector2D()
    
    closeObstacles.foreach { obstacle =>  
      val distance = boid.distance(obstacle)
        
      if (distance > 0.0) {
        var force = Vector2D(boid.position, obstacle.position).normalized
        force = force.scaled(1.0 - this.obstacleRepulsionDistance / distance)
        obstacleAvoidSteer = obstacleAvoidSteer + force
      }
    }
      
    if (obstacleAvoidSteer.length != 0.0){
      obstacleAvoidSteer = obstacleAvoidSteer * (1.0 / closeObstacles.length)
      obstacleAvoidSteer = obstacleAvoidSteer.scaled(boid.maxSpeed)
      obstacleAvoidSteer = obstacleAvoidSteer - boid.velocity
      }
    
    obstacleAvoidSteer.normalized
  }
  
  /** This is the overwritten updating method which makes everything work.
    * 
    * Basically, at each frame calculates all necessairy forces and other 
    * interactions, applies forces and moves the boids according to their 
    * velocities. In this implementation each steering force is simply added 
    * together and limited to the boids max force, except for the obstacle
    * avoidance force.
    */
  override def updateAll: Unit = {
    var x = 0
    //For each boid this calculation is done in parallel
    this.boids.par.foreach { boid =>
      
      //first calculate neighbours and close obstacles
      val neighbours = this.boidsInVision(boid)
      val closeObstacles = this.obstacles.filter { obstacle  => 
        boid.distance(obstacle) < this.obstacleRepulsionDistance 
        }
      
      //Each force to calculate: separation, cohesion, alignement, obstacle
      //avoidance and mouse attraction/repulsion
      var separateSteer = Vector2D()
      var cohesionSteer = Vector2D()
      var AlignSteer = Vector2D()
      var obstacleSteer = Vector2D()
      var mouseSteer = Vector2D()
       
      //Calculate obstacle avoidance
      if (closeObstacles.size > 0){
        obstacleSteer = this.avoidObstacle(boid, closeObstacles) 
      } 
      
      //Calculate mouse attraction/repulsion
      if(this.attractiveMouse){
        if (boid.inRange(this.mouseLocation, this.mouseAttractionRange))
          mouseSteer = this.seekPos(boid, this.mouseLocation) 
              
      } else if (this.repulsiveMouse){
        if (boid.inRange(this.mouseLocation, this.mouseAttractionRange))
         mouseSteer = this.avoidPos(boid, this.mouseLocation)
      }
      
      //Calculate separation, cohesion and alignement
      if (neighbours.boids.length > 0){
        separateSteer = this.separate(boid, neighbours)
        cohesionSteer = this.cohesion(boid, neighbours)
        AlignSteer = this.align(boid, neighbours) 
        }
      
      //Finally, add forces together. Everything else is limited to the boid's
      //max force except the obstacle avoidance force
      var resultantForce = 
        separateSteer * this.separationFactor +
        cohesionSteer * this.cohesionFactor +
        AlignSteer * this.alignementFactor + 
        mouseSteer * this.mouseAttractionFactor 
        
      resultantForce = resultantForce.limited(boid.maxForce) + 
        obstacleSteer * this.obstacleRepulsionFactor
        
      //Finally, apply the resultant force
      boid.applyForce(resultantForce)
      x = x+1
      }

    println(x)
    //After all the forces are applied, boids can be moved
    this.moveAll
  }
}



