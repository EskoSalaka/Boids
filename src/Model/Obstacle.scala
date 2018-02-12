package Model
import scala.swing.Graphics2D
import java.awt.Color
import java.awt.geom.Ellipse2D
  

/** A simple class to describe an obstacle.
  * 
  * It is basically considered a round object having a radius and a position.
  * 
  * @constructor create a new Boid with given characteristics
  * @param position the position of the Obstacle
  * @param radius the radius of the Obstacle
  * @author Esko-Kalervo Salaka
  */
class Obstacle(var position: Position2D, 
               var radius: Double) {
  
  /** Draws the Obstacle using a given Graphics2D instance.
    */
  def draw(g: Graphics2D): Unit = {
    g.setColor(Color.BLACK)
    val shape1 = new Ellipse2D.Double(this.position.x - radius, 
                                      this.position.y - radius, 
                                      radius * 2, radius * 2)
  
    g.draw(shape1)
  }
}

/** A Factory for Obstacle.
  */
object Obstacle{
  
  def apply(position: Position2D, radius: Double): Obstacle = 
    new Obstacle(position, radius)
  
}