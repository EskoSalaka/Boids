package GUI

import scala.swing.{Graphics2D, Panel}
import java.awt.{Color, GradientPaint, RenderingHints}

/** A simple Panel class for use a painting canvas.
  * @param UI the parent user interface 
  * @author Esko-Kalervo Salaka
  */
class PaintCanvas(UI: UI) extends Panel{
  
  /** Paints the bacground using a given Graphics2D object.
    */
  private def paintBackground(g: Graphics2D): Unit = {
    g.setColor(Color.WHITE)
    g.fillRect(0,0,this.size.width,this.size.height)
  }
  
  /** Paints the Boids and Obstacles of the simulation flock of the main UI.
    */
  private def paintBoids(g: Graphics2D): Unit = {
    this.UI.simFlock.drawBoids(g)
    this.UI.simFlock.drawObstacles(g)
  }
  
  /** Overwritten Paint method to draw Boids and Obstacles at each frame. 
    */
  override def paint(g: Graphics2D): Unit = {
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                       RenderingHints.VALUE_ANTIALIAS_ON)
    this.paintBackground(g)
    this.paintBoids(g)
    
  }
  
  
  
  
}
  