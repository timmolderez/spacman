package be.vub.spacman.board

import be.vub.spacman.UnitSpec
import org.scalamock.scalatest.MockFactory

/**
  * Test various aspects of board.
  *
  * @author Jeroen Roosen
  */
object BoardSpec {
  private val MAX_WIDTH = 2
  private val MAX_HEIGHT = 3
}

class BoardSpec extends UnitSpec with MockFactory {
  private var board = null : Board
  final private val x0y0 = mock[Square]
  final private val x0y1 = mock[Square]
  final private val x0y2 = mock[Square]
  final private val x1y0 = mock[Square]
  final private val x1y1 = mock[Square]
  final private val x1y2 = mock[Square]

  /**
    * Setup a board that can be used for testing.
    */
  override def withFixture(test: NoArgTest) = {
    // Test setup
    val grid = Array.ofDim[Square](BoardSpec.MAX_WIDTH, BoardSpec.MAX_HEIGHT)
    grid(0)(0) = x0y0
    grid(0)(1) = x0y1
    grid(0)(2) = x0y2
    grid(1)(0) = x1y0
    grid(1)(1) = x1y1
    grid(1)(2) = x1y2
    board = new Board(grid)

    // Run the test
    try test()
  }

  "Board width" should "be MAX_WIDTH" in {
    board.getWidth should be (BoardSpec.MAX_WIDTH)
  }

  "Board height" should "be MAX_HEIGHT" in {
    board.getHeight should be (BoardSpec.MAX_HEIGHT)
  }

  "Square in 1st row, 1st column" should "be x0y0" in {
    board.squareAt(0,0) should be (x0y0)
  }

  "Square in 2nd column, 3rd row" should "be x1y2" in {
    board.squareAt(1,2) should be (x1y2)
  }

  "Square in 1st column, 2nd row" should "be x0y1" in {
    board.squareAt(0,1) should be (x0y1)
  }

  "Invalid square coordinates" should "throw an exception" in {
    a [ArrayIndexOutOfBoundsException] should be thrownBy{board.squareAt(-1, -1)}
  }

  "Square in 2nd column, 3rd row" should "be within the board's borders" in {
    board.withinBorders(1,2) should be (true)
  }

  "Invalid square" should "be outside the board's borders" in {
    board.withinBorders(-1, -1) should be (false)
  }

  "Square in 3rd column, 3rd row" should "be outside the board's borders" in {
    board.withinBorders(2,2) should be (false)
  }

}
