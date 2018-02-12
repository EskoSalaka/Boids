package GUI

import scala.swing.{MainFrame, Dimension, BorderPanel, Component}
import scala.swing.event.{UIElementResized, MousePressed, MouseDragged, 
                          MouseMoved}
import javax.swing.{SpinnerNumberModel, JSpinner, JLabel, JToolBar, JButton,
                    Timer, SwingConstants, ImageIcon, JRadioButton, 
                    JToggleButton}
import java.awt.event.{ActionListener, ActionEvent}
import javax.swing.event.{ChangeEvent, ChangeListener}

import java.awt.Font

import Model.{Boid, SimpleSim, Position2D, Obstacle}

/** The MainFrame of the GUI.
  *  
  * Basically, this class is just an uninteresting bunch of GUI crap. It has 
  * nothing to do with the actual simulation model itself.
  *  
  * @author Esko-Kalervo Salaka
  */
class UI extends MainFrame{
  
  private val timer = new Timer(30,  new ActionListener {
    def actionPerformed(e: ActionEvent) = {
      simulationTick()
    }
  }) 
  
  val simFlock = new SimpleSim(Vector(),Vector(),500,500,1,1,1,1,1,0.07,30)
  
  private def simulationTick(): Unit = {
    this.simFlock.updateAll
    this.paintCanvas.repaint()
  }
  
  def getBoidFromSpinnerValues(pos: Position2D): Boid = {
    val visionDistance = this.visionDistanceSpinner.getValue.toString().toDouble 
    val maxForce = this.maxForceSpinner.getValue.toString().toDouble 
    val maxSpeed = this.maxSpeedSpinner.getValue.toString().toDouble 
    val visionAngle = this.visionAngleSpinner.getValue.toString().toDouble 
    val repulsionDistance = this.repulsionDistanceSpinner.getValue.toString().toDouble 
    
    Boid.getRandomBoid(pos, visionAngle, 
                       repulsionDistance, visionDistance, 
                       maxSpeed, maxForce)
  }
  
  def getBoidFromSpinnerValues(xMax: Double, yMax: Double): Boid = {
    val visionDistance = this.visionDistanceSpinner.getValue.toString().toDouble 
    val maxForce = this.maxForceSpinner.getValue.toString().toDouble 
    val maxSpeed = this.maxSpeedSpinner.getValue.toString().toDouble 
    val visionAngle = this.visionAngleSpinner.getValue.toString().toDouble 
    val repulsionDistance = this.repulsionDistanceSpinner.getValue.toString().toDouble 
    
    Boid.getRandomPosBoid(0, 0, xMax.toInt, yMax.toInt, visionAngle, 
                          repulsionDistance, visionDistance, 
                          maxSpeed, maxForce)
  }
  
  def setDefaultValues(): Unit = {
    val cohesionFactor = 0.5
    val alignmentFactor = 1.5
    val separationFactor = 3.0
    
    val visionAngle = 90.0
    val visionDistance = 55.0
    val repulsionDistance = 20.0
    val maxForce = 0.2
    val maxSpeed = 5
    
    val mouseAttractionRange = 200.0
    val mouseAttractionFactor = 0.4
    
    val obstacleRepDistance = 30.0
    val obstacleRepFactor = 0.4
    
    this.cohesionSpinner.setValue(cohesionFactor)
    this.alignementSpinner.setValue(alignmentFactor)
    this.separationSpinner.setValue(separationFactor)
    
    this.visionAngleSpinner.setValue(visionAngle)
    this.visionDistanceSpinner.setValue(visionDistance)
    this.repulsionDistanceSpinner.setValue(repulsionDistance)
    this.maxForceSpinner.setValue(maxForce)
    this.maxSpeedSpinner.setValue(maxSpeed)
    
    this.mouseAttractionFactorSpinner.setValue(mouseAttractionFactor)
    this.mouseAttractionRangeSpinner.setValue(mouseAttractionRange)
    
    this.obstacleRepDistanceSpinner.setValue(obstacleRepDistance)
    this.obstacleRepulsionFactorSpinner.setValue(obstacleRepFactor)
  }
  
  def restartSimulation(): Unit = {
    this.simFlock.clear()
    val startingBoids = this.startingBoidsSpinner.getValue.toString().toDouble 
    val width = this.paintCanvas.size.getWidth.toInt
    val height = this.paintCanvas.size.getHeight.toInt
    
    if (this.randomRadioButton.isSelected()){
      for (x <- 0 to startingBoids.toInt){
        
        this.simFlock.addBoid(this.getBoidFromSpinnerValues(width, height))
      }
    } else {
      for (x <- 0 to startingBoids.toInt){
        val pos = Position2D(width/2.0, height/2.0)
        
        this.simFlock.addBoid(this.getBoidFromSpinnerValues(pos))
      }
    }
  }
  
  
  //----------------------------------------------------------------------------
  //Setting up the UI
  //----------------------------------------------------------------------------
  
  private def getSpinner(minVal: Double, 
                         maxVal: Double, 
                         stepVal: Double): JSpinner = {
    val prefSize = new Dimension(50, 20)
    val spinner = new JSpinner()
    val model = new SpinnerNumberModel(100, minVal, maxVal, stepVal)
    
    spinner.setModel(model)
    spinner.setMaximumSize(prefSize)
    spinner.setMinimumSize(prefSize)
    spinner.setPreferredSize(prefSize)
    
    spinner
  }
  
  title = "Boids"
  size = new Dimension(1600,1000)
  preferredSize = size
  visible = true
  
  //Setting up side toolbar
  private val sideToolbar = new JToolBar(SwingConstants.VERTICAL)
  
  
  private val cohesionSpinner = this.getSpinner(0.0, 100.0, 0.1)
  private val cohesionSpinnerLabel = new JLabel("Cohesion factor:")
  
  private val separationSpinner = this.getSpinner(0.0, 100.0, 0.1)
  private val separationSpinnerLabel = new JLabel("Separation factor:")
  
  private val alignementSpinner = this.getSpinner(0.0, 100.0, 0.1)
  private val alignementSpinnerLabel = new JLabel("Alignement factor:")
  
  private val visionDistanceSpinner = this.getSpinner(0.0, 1000.0, 0.5)
  private val visionDistanceSpinnerLabel = new JLabel("Vision distance:")
  
  private val visionAngleSpinner = this.getSpinner(0.0, 180.0, 0.5)
  private val visionAngleSpinnerLabel = new JLabel("Vision angle:")
  
  private val maxForceSpinner = this.getSpinner(0.0, 100.0, 0.01)
  private val maxForceSpinnerLabel = new JLabel("Max force:")
  
  private val maxSpeedSpinner = this.getSpinner(0.0, 100.0, 0.1)
  private val maxSpeedSpinnerLabel = new JLabel("Max speed:")
  
  private val repulsionDistanceSpinner = this.getSpinner(0.0, 100.0, 0.1)
  private val repulsionDistanceSpinnerLabel = new JLabel("Repulsion distance:")
  
  private val obstacleRepDistanceSpinner = this.getSpinner(0.0, 100.0, 0.1)
  private val obstacleRepDistanceSpinnerLabel = 
    new JLabel("Obstacle repulsion distance: ")
  
  private val obstacleRepulsionFactorSpinner = this.getSpinner(0.0, 100.0, 0.01)
  private val obstacleRepulsionFactorSpinnerLabel = 
    new JLabel("Obstacle repulsion factor: ")
  
  private val mouseAttractionRangeSpinner = this.getSpinner(0.0, 100.0, 0.1)
  private val mouseAttractionRangeSpinnerLabel = 
    new JLabel("Mouse attraction distance: ")
  
  private val mouseAttractionFactorSpinner = this.getSpinner(0.0, 100.0, 0.01)
  private val mouseAttractionFactorSpinnerLabel = 
    new JLabel("Mouse attraction factor: ")
  
  private val simLabel = new JLabel("Simulation specific factors")
  simLabel.setFont(new Font("Serif", Font.BOLD, 16))
  
  private val boidLabel = new JLabel("Boid specific factors")
  boidLabel.setFont(new Font("Serif", Font.BOLD, 16))
  
  private val obstacleLabel = new JLabel("Obstacle forcefield adjustment")
  obstacleLabel.setFont(new Font("Serif", Font.BOLD, 16))
  
  private val mouseLabel = new JLabel("Attractive/repuslive mouse adjustment: ")
  obstacleLabel.setFont(new Font("Serif", Font.BOLD, 16))
  
  private var restoreDefaultsButton = new JButton("Restore defaults")
  
  sideToolbar.add(restoreDefaultsButton)
  sideToolbar.addSeparator()
  
  sideToolbar.add(simLabel)
  sideToolbar.addSeparator()
  
  sideToolbar.add(cohesionSpinnerLabel)
  sideToolbar.add(cohesionSpinner)
  
  sideToolbar.add(separationSpinnerLabel)
  sideToolbar.add(separationSpinner)
  
  sideToolbar.add(alignementSpinnerLabel)
  sideToolbar.add(alignementSpinner)
  
  sideToolbar.addSeparator()
  sideToolbar.add(boidLabel)
  sideToolbar.addSeparator()
  
  sideToolbar.add(visionDistanceSpinnerLabel)
  sideToolbar.add(visionDistanceSpinner)
  
  sideToolbar.add(visionAngleSpinnerLabel)
  sideToolbar.add(visionAngleSpinner)
  
  sideToolbar.add(maxForceSpinnerLabel)
  sideToolbar.add(maxForceSpinner)
  
  sideToolbar.add(maxSpeedSpinnerLabel)
  sideToolbar.add(maxSpeedSpinner)
  
  sideToolbar.add(repulsionDistanceSpinnerLabel)
  sideToolbar.add(repulsionDistanceSpinner)
  
  sideToolbar.addSeparator()
  sideToolbar.add(mouseLabel)
  sideToolbar.addSeparator()
  
  sideToolbar.add(mouseAttractionFactorSpinnerLabel)
  sideToolbar.add(mouseAttractionFactorSpinner)
  
  sideToolbar.add(mouseAttractionRangeSpinnerLabel)
  sideToolbar.add(mouseAttractionRangeSpinner)
  
  
  sideToolbar.addSeparator()
  sideToolbar.add(obstacleLabel)
  sideToolbar.addSeparator()
  
  sideToolbar.add(obstacleRepulsionFactorSpinnerLabel)
  sideToolbar.add(obstacleRepulsionFactorSpinner)
  
  sideToolbar.add(obstacleRepDistanceSpinnerLabel)
  sideToolbar.add(obstacleRepDistanceSpinner)
  
  
  //Top toolbar
  private val topToolbar = new JToolBar()
  
  private val pauseIcon = new ImageIcon(getClass.getResource("Icons/pause.png"))
  private val pauseButton = new JButton(pauseIcon)
  
  private val playIcon = new ImageIcon(getClass.getResource("Icons/play.png"))
  private val playButton = new JButton(playIcon)
  
  private val restartIcon = new ImageIcon(getClass.getResource("Icons/restart.png"))
  private val restartButton = new JButton(restartIcon)
  
  private val startingBoidsSpinner = this.getSpinner(0, 10000, 1)
  private val startingBoidsSpinnerLabel = new JLabel("Starting boids:")
  
  private val tickRateSpinner = this.getSpinner(0, 10000, 1)
  tickRateSpinner.setValue(30)
  private val tickRateSpinnerLabel = new JLabel("Tickrate(ms): ")
  
  private val randomRadioButton = new JRadioButton("Boids start at random")
  randomRadioButton.setSelected(true)
  private val middleRadioButton = new JRadioButton("Boids start in the middle")
  
  private var attractiveMouseButton = new JToggleButton("Attractive mouse")
  private var repulsiveMouseButton = new JToggleButton("Repulsive mouse")
  
  topToolbar.add(playButton)
  topToolbar.add(pauseButton)
  topToolbar.add(restartButton)
  topToolbar.addSeparator()
  
  topToolbar.add(startingBoidsSpinnerLabel)
  topToolbar.add(startingBoidsSpinner)
  topToolbar.addSeparator()
  
  topToolbar.add(randomRadioButton)
  topToolbar.add(middleRadioButton)
  topToolbar.addSeparator()
  
  topToolbar.add(attractiveMouseButton)
  topToolbar.add(repulsiveMouseButton)
  topToolbar.addSeparator()
  
  topToolbar.add(tickRateSpinnerLabel)
  topToolbar.add(tickRateSpinner)
  topToolbar.addSeparator()

  //Setting up layout
  private val paintCanvas = new PaintCanvas(this)
  
  contents = new BorderPanel(){
    //PaintCanvas
    layout(paintCanvas) = BorderPanel.Position.Center
    
    //toolbar
    layout(Component.wrap(sideToolbar)) = BorderPanel.Position.East
    layout(Component.wrap(topToolbar)) = BorderPanel.Position.North
  }
    
  //----------------------------------------------------------------------------
  //Event handling
  //----------------------------------------------------------------------------
  
  //Sim specific spinners
  separationSpinner.addChangeListener(new ChangeListener {
    def stateChanged(e: ChangeEvent): Unit = 
      simFlock.separationFactor = separationSpinner.getValue.toString().toDouble
    
  })
  
  cohesionSpinner.addChangeListener(new ChangeListener {
    def stateChanged(e: ChangeEvent): Unit = 
      simFlock.cohesionFactor = cohesionSpinner.getValue.toString().toDouble
      
  })
  
  alignementSpinner.addChangeListener(new ChangeListener {
    def stateChanged(e: ChangeEvent): Unit = 
      simFlock.alignementFactor = alignementSpinner.getValue.toString().toDouble
      
  })
  
  //Boid specific spinners
  visionAngleSpinner.addChangeListener(new ChangeListener {
    def stateChanged(e: ChangeEvent): Unit = 
      simFlock.setVisionAngle(visionAngleSpinner.getValue.toString().toDouble) 
  })
  
  repulsionDistanceSpinner.addChangeListener(new ChangeListener {
    def stateChanged(e: ChangeEvent): Unit = 
      simFlock.setRepulsionDistance(repulsionDistanceSpinner.getValue.toString().toDouble) 
  })
  
  visionDistanceSpinner.addChangeListener(new ChangeListener {
    def stateChanged(e: ChangeEvent): Unit = 
      simFlock.setVisionDistance(visionDistanceSpinner.getValue.toString().toDouble) 
  })
  
  maxSpeedSpinner.addChangeListener(new ChangeListener {
    def stateChanged(e: ChangeEvent): Unit = 
      simFlock.setMaxSpeed(maxSpeedSpinner.getValue.toString().toDouble) 
  })
  
  maxForceSpinner.addChangeListener(new ChangeListener {
    def stateChanged(e: ChangeEvent): Unit = 
      simFlock.setMaxForce(maxForceSpinner.getValue.toString().toDouble) 
  })
  
  restoreDefaultsButton.addActionListener( new ActionListener {
    def actionPerformed(e: ActionEvent) = 
      setDefaultValues()
  }) 
  
  //top toolbar buttons
  playButton.addActionListener( new ActionListener {
    def actionPerformed(e: ActionEvent) = 
      timer.start()
  }) 
  
  pauseButton.addActionListener( new ActionListener {
    def actionPerformed(e: ActionEvent) = 
      timer.stop()
  }) 
  
  restartButton.addActionListener( new ActionListener {
    def actionPerformed(e: ActionEvent) = 
      restartSimulation()
  }) 
  
  tickRateSpinner.addChangeListener(new ChangeListener {
    def stateChanged(e: ChangeEvent): Unit = 
      timer.setDelay(tickRateSpinner.getValue.toString().toDouble.toInt)
  })
  
  mouseAttractionFactorSpinner.addChangeListener(new ChangeListener {
    def stateChanged(e: ChangeEvent): Unit = 
      simFlock.mouseAttractionFactor = 
        (mouseAttractionFactorSpinner.getValue.toString().toDouble)
  })
  
  mouseAttractionRangeSpinner.addChangeListener(new ChangeListener {
    def stateChanged(e: ChangeEvent): Unit = 
      simFlock.mouseAttractionRange = 
        (mouseAttractionRangeSpinner.getValue.toString().toDouble)
  })
  
  obstacleRepDistanceSpinner.addChangeListener(new ChangeListener {
    def stateChanged(e: ChangeEvent): Unit = 
      simFlock.obstacleRepulsionDistance = 
        (obstacleRepDistanceSpinner.getValue.toString().toDouble)
  })
  
  obstacleRepDistanceSpinner.addChangeListener(new ChangeListener {
    def stateChanged(e: ChangeEvent): Unit = 
      simFlock.obstacleRepulsionDistance = 
        (obstacleRepDistanceSpinner.getValue.toString().toDouble)
  })
  
  obstacleRepulsionFactorSpinner.addChangeListener(new ChangeListener {
    def stateChanged(e: ChangeEvent): Unit = 
      simFlock.obstacleRepulsionFactor = 
        (obstacleRepulsionFactorSpinner.getValue.toString().toDouble)
  })
  
  randomRadioButton.addActionListener(new ActionListener {
    def actionPerformed(e: ActionEvent): Unit = 
      if (randomRadioButton.isEnabled()) middleRadioButton.setSelected(false)
  })
  
  middleRadioButton.addActionListener(new ActionListener {
    def actionPerformed(e: ActionEvent): Unit = 
      if (middleRadioButton.isEnabled()) randomRadioButton.setSelected(false)
  })
  
  attractiveMouseButton.addActionListener(new ActionListener {
    def actionPerformed(e: ActionEvent) = {
      if (repulsiveMouseButton.isSelected()){
        attractiveMouseButton.setSelected(true)
        repulsiveMouseButton.setSelected(false)
      } else if (!attractiveMouseButton.isSelected() & 
                 !repulsiveMouseButton.isSelected()) {
        attractiveMouseButton.setSelected(false)
      } else {
        attractiveMouseButton.setSelected(true)
      }
      
      simFlock.attractiveMouse = attractiveMouseButton.isSelected()
      simFlock.repulsiveMouse = repulsiveMouseButton.isSelected()
      println(simFlock.attractiveMouse, simFlock.repulsiveMouse)
    }
  }) 
  
  repulsiveMouseButton.addActionListener(new ActionListener {
    def actionPerformed(e: ActionEvent) = {
      if (attractiveMouseButton.isSelected()){
        attractiveMouseButton.setSelected(false)
        repulsiveMouseButton.setSelected(true)
      } else if (!attractiveMouseButton.isSelected() & 
                 !repulsiveMouseButton.isSelected()) {
        repulsiveMouseButton.setSelected(false)
      } else {
        repulsiveMouseButton.setSelected(true)
      }
      simFlock.attractiveMouse = attractiveMouseButton.isSelected()
      simFlock.repulsiveMouse = repulsiveMouseButton.isSelected()
      println(simFlock.attractiveMouse, simFlock.repulsiveMouse)
    }
  }) 
  
  listenTo(this, this.paintCanvas.mouse.clicks, this.paintCanvas.mouse.moves)
  
  reactions += {
    case UIElementResized(ui) => onResize()
    case mp: MousePressed => onMouseClicked(mp)
    case md: MouseDragged => onMouseDragged(md)
    case mm: MouseMoved => onMouseMoved(mm)
    
  }
  
  private def onResize(): Unit = {
    this.simFlock.xBound = paintCanvas.size.width
    this.simFlock.yBound = paintCanvas.size.height
  }
  
  private def onMouseClicked(mp: MousePressed): Unit = {
    val pos = Position2D(mp.point.getX, mp.point.getY)
    
    if (mp.modifiers == 1024){
      this.simFlock.addBoid(this.getBoidFromSpinnerValues(pos))
    } else if (mp.modifiers == 1024) {
      this.simFlock.addObstacle(Obstacle(pos, 5))
    }
  }
  
  private def onMouseDragged(md: MouseDragged): Unit = {
    val pos = Position2D(md.point.getX, md.point.getY)

    if (md.modifiers == 1024){
      this.simFlock.addBoid(this.getBoidFromSpinnerValues(pos))
    } else if (md.modifiers == 4096 ) {
      this.simFlock.addObstacle(Obstacle(pos, 5))
    }
    
    this.simFlock.mouseLocation = pos
  }
  
  private def onMouseMoved(mm: MouseMoved): Unit = 
    this.simFlock.mouseLocation = Position2D(mm.point.getX, mm.point.getY)
  
  
  
  this.setDefaultValues()
  this.restartSimulation()
  this.timer.start()
  
}