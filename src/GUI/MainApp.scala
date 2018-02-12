package GUI

import scala.swing.SimpleSwingApplication

/** This simple Object just runs the GUI. Use this to start the simulation.
  *  
  * @author Esko-Kalervo Salaka
  */
object MainApp extends SimpleSwingApplication {
  
  def top = new UI()
}