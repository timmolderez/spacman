package be.vub.spacman.ui
package nl.tudelft.jpacman.ui

import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import javax.swing.JPanel

import be.vub.spacman.board.Board
import be.vub.spacman.board.nl.tudelft.jpacman.board.Square
import be.vub.spacman.game.nl.tudelft.jpacman.game.Game

/**
  * Panel displaying a game.
  *
  * @author Jeroen Roosen
  *
  */
@SerialVersionUID(1L)
object BoardPanel {
  /**
    * The background colour of the board.
    */
  private val BACKGROUND_COLOR = Color.BLACK
  /**
    * The size (in pixels) of a square on the board. The initial size of this
    * panel will scale to fit a board with square of this size.
    */
  private val SQUARE_SIZE = 16
}

@SerialVersionUID(1L)
class BoardPanel private[ui](val game: Game) extends JPanel {
  assert(game != null)
  val board: Board = game.getLevel.getBoard
  val w: Int = board.getWidth * BoardPanel.SQUARE_SIZE
  val h: Int = board.getHeight * BoardPanel.SQUARE_SIZE
  override val size = new Dimension(w, h)
  setMinimumSize(size)
  setPreferredSize(size)

  override def paint(g: Graphics): Unit = {
    assert(g != null)
    render(game.getLevel.getBoard, g, getSize)
  }

  /**
    * Renders the board on the given graphics context to the given dimensions.
    *
    * @param board
    * The board to render.
    * @param g
    * The graphics context to draw on.
    * @param window
    * The dimensions to scale the rendered board to.
    */
  private def render(board: Board, g: Graphics, window: Dimension): Unit = {
    val cellW = window.width / board.getWidth
    val cellH = window.height / board.getHeight
    g.setColor(BoardPanel.BACKGROUND_COLOR)
    g.fillRect(0, 0, window.width, window.height)
    var y = 0
    while ( {
      y < board.getHeight
    }) {
      var x = 0
      while ( {
        x < board.getWidth
      }) {
        val cellX = x * cellW
        val cellY = y * cellH
        val square = board.squareAt(x, y)
        render(square, g, cellX, cellY, cellW, cellH)

        {
          x += 1; x - 1
        }
      }

      {
        y += 1; y - 1
      }
    }
  }

  /**
    * Renders a single square on the given graphics context on the specified
    * rectangle.
    *
    * @param square
    * The square to render.
    * @param g
    * The graphics context to draw on.
    * @param x
    * The x position to start drawing.
    * @param y
    * The y position to start drawing.
    * @param w
    * The width of this square (in pixels.)
    * @param h
    * The height of this square (in pixels.)
    */
  private def render(square: Square, g: Graphics, x: Int, y: Int, w: Int, h: Int): Unit = {
    square.getSprite.draw(g, x, y, w, h)
    import scala.collection.JavaConversions._
    for (unit <- square.getOccupants) {
      unit.getSprite.draw(g, x, y, w, h)
    }
  }
}
