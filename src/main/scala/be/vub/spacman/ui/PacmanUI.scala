package be.vub.spacman.ui
package nl.tudelft.jpacman.ui

import java.awt.BorderLayout
import java.awt.Container
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.swing.JFrame

import be.vub.spacman.game.nl.tudelft.jpacman.game.Game
import be.vub.spacman.ui.nl.tudelft.jpacman.ui.ScorePanel.ScoreFormatter


/**
  * The default JPacMan UI frame. The PacManUI consists of the following
  * elements:
  *
  * <ul>
  * <li>A score panel at the top, displaying the score of the player(s).
  * <li>A board panel, displaying the current level, i.e. the board and all units
  * on it.
  * <li>A button panel, containing all buttons provided upon creation.
  * </ul>
  *
  * @author Jeroen Roosen
  *
  */
@SerialVersionUID(1L)
object PacmanUI {
  /**
    * The desired frame rate interval for the graphics in milliseconds, 40
    * being 25 fps.
    */
  private val FRAME_INTERVAL = 40
}

@SerialVersionUID(1L)
class PacmanUI(val game: Game, val buttons: Map[String, Action], val keyMappings: Map[Integer, Action], val sf: ScoreFormatter) extends JFrame("JPac-Man") {
  assert(game != null)
  assert(buttons != null)
  assert(keyMappings != null)
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  val keys = new PacKeyListener(keyMappings)
  addKeyListener(keys)
  val buttonPanel = new ButtonPanel(buttons, this)
  scorePanel = new ScorePanel(game.getPlayers)
  if (sf != null) scorePanel.setScoreFormatter(sf)
  boardPanel = new BoardPanel(game)
  val contentPanel: Container = getContentPane
  contentPanel.setLayout(new BorderLayout)
  contentPanel.add(buttonPanel, BorderLayout.SOUTH)
  contentPanel.add(scorePanel, BorderLayout.NORTH)
  contentPanel.add(boardPanel, BorderLayout.CENTER)
  pack()

  /** The panel displaying the player scores. */
  final private var scorePanel = null : ScorePanel
  /** The panel displaying the game. */
  final private var boardPanel = null : BoardPanel

  /**
    * Starts the "engine", the thread that redraws the interface at set
    * intervals.
    */
  def start(): Unit = {
    setVisible(true)
    val service = Executors.newSingleThreadScheduledExecutor
    service.scheduleAtFixedRate(new Runnable() {
      override def run(): Unit = {
        nextFrame()
      }
    }, 0, PacmanUI.FRAME_INTERVAL, TimeUnit.MILLISECONDS)
  }

  /**
    * Draws the next frame, i.e. refreshes the scores and game.
    */
  private def nextFrame(): Unit = {
    boardPanel.repaint()
    scorePanel.refresh()
  }
}
