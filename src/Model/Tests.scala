package Model
import math.{sqrt}
import org.junit.Test
import org.junit.Assert._
import scala.util.Random

/** A class for unit testing the most important parts of the program, which 
  * are hard to determine otheriwise.
  *  
  * @author Esko-Kalervo Salaka
  */
class UnitTests {
  val pos1 = Position2D(1.0,1.0)
  val pos2 = Position2D(0,1)
  val pos3 = Position2D(1,0)
  val pos4 = Position2D(5,5)
  val pos5 = Position2D(100,100)
  val pos6 = Position2D(0.1,0)
  val pos7 = Position2D(0.5,0.5)
  val pos8 = Position2D(-0.5,-0.5)
  val pos9 = Position2D(-1,-1)
  val v1 = Vector2D(1.0,1.0)
  val v2 = Vector2D(0,1)
  val v3 = Vector2D(1,0)
  val v4 = Vector2D(5,5)
  val v5 = Vector2D(100,100)
  val v6 = Vector2D(0.1,0)
  val v7 = Vector2D(0.5,0.5)
  val v8 = Vector2D(-0.5,-0.5)
  val v9 = Vector2D(-1,-1)


  @Test def lengths() {
    assertEquals(v1.length, sqrt(2), 0.000001)
    assertEquals(v2.length, 1.0, 0.000001)
    assertEquals(v3.length, 1, 0.000001)
    assertEquals(v4.length, sqrt(50), 0.000001)
    assertEquals(v5.length, sqrt(20000), 0.000001)

    assertEquals(v1.normalized.length, 1, 0.000001)
    assertEquals(v2.normalized.length, 1.0, 0.000001)
    assertEquals(v3.normalized.length, 1, 0.000001)
    assertEquals(v4.normalized.length, 1, 0.000001)
    assertEquals(v5.normalized.length, 1.0, 0.000001)
    assertEquals(v6.normalized.length, 1.0, 0.000001)
    assertEquals(v7.normalized.length, 1.0, 0.000001)
    assertEquals(v8.normalized.length, 1.0, 0.000001)
    assertEquals(v9.normalized.length, 1.0, 0.000001)
    
    assertEquals(v1.limited(1).length, 1, 0.000001)
    assertEquals(v9.limited(1).length, 1, 0.000001)
  }
  /** Testing angles
    */
  @Test def angles() {
    assertEquals(v1.angle(v1), 0.0, 0.0001)
    assertEquals(v1.angle(v4), 0.0, 0.0001)
    assertEquals(v1.angle(v2), 45, 0.0001)
    assertEquals(v2.angle(v1), 45, 0.0001)
    assertEquals(v1.angle(v7), 0, 0.0001)
    assertEquals(v1.angle(v9), 180, 0.0001)
    assertEquals(v1.angle(v1.reversed), 180, 0.0001)
    assertEquals(v2.angle(v6.reversed), 90, 0.0001)
  }
  
  @Test def randoms() {
    var totVector = Vector2D()
    var positions: Vector[Position2D] = Vector()
    var vectors: Vector[Vector2D] = Vector()
    
    for (x <- 0 to 1000000){
      val v1 = Vector2D.getRandomUnit()
      val v2 = Vector2D.getRandomUnit()
      
      val p1 = Position2D.getRandom(-1, -1, 1, 1)
      val p2 = Position2D.getRandom(-100.0, -100.0, 100.0, 100.0)
      totVector = totVector + v1
      assertTrue("", v1.angle(v2) <= 180 & v1.angle(v2) >= 0)
      assertTrue("", p1.x <= 1 & p1.x >= -1 & p1.y <= 1 & p1.y >= -1)
      assertEquals(v1.length, 1, 0.000001)
      positions = positions :+ p1
      vectors = vectors :+ v1 :+ v2
    }
    
    println(totVector.x, totVector.y)
    println(Position2D.average(positions).x, Position2D.average(positions).y)
    println(Vector2D.average(vectors).x, Vector2D.average(vectors).y)
  }
  
}