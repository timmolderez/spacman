package be.vub.spacman.board

import be.vub.spacman.UnitSpec
import be.vub.spacman.sprite.PacManSprites
import org.scalamock.scalatest.MockFactory

/**
  * Tests the linking of squares done by the board factory.
  *
  * @author Jeroen Roosen
  */
class BoardFactorySpec extends UnitSpec with MockFactory {

  /** The factory under test. */
  private var factory = null : BoardFactory

  override def withFixture(test: NoArgTest) = {
    // Test setup
    val sprites = mock[PacManSprites]
    factory = new BoardFactory(sprites)

    // Run the test
    try test()
  }

  "Single cell" should "be connected to itself on all sides" in {
    val s = new BasicSquare
    val grid = Array[Array[Square]](Array(s))
    factory.createBoard(grid)

    s.getSquareAt(Directions.NORTH) should be (s)
    s.getSquareAt(Directions.SOUTH) should be (s)
    s.getSquareAt(Directions.WEST) should be (s)
    s.getSquareAt(Directions.EAST) should be (s)
  }

  "Chain of cells" should "be connected to the east" in {
    val s1 = new BasicSquare
    val s2 = new BasicSquare
    val grid = Array[Array[Square]](Array(s1), Array(s2))
    factory.createBoard(grid)
    s1.getSquareAt(Directions.EAST) should be (s2)
    s2.getSquareAt(Directions.EAST) should be (s1)
  }

  "Chain of cells" should "be connected to the west" in {
    val s1 = new BasicSquare
    val s2 = new BasicSquare
    val grid = Array[Array[Square]](Array(s1), Array(s2))
    factory.createBoard(grid)
    s1.getSquareAt(Directions.WEST) should be (s2)
    s2.getSquareAt(Directions.WEST) should be (s1)
  }

  "Chain of cells" should "be connected to the north" in {
    val s1 = new BasicSquare
    val s2 = new BasicSquare
    val grid = Array[Array[Square]](Array(s1, s2))
    factory.createBoard(grid)
    s1.getSquareAt(Directions.NORTH) should be (s2)
    s2.getSquareAt(Directions.NORTH) should be (s1)
  }

  "Chain of cells" should "be connected to the south" in {
    val s1 = new BasicSquare
    val s2 = new BasicSquare
    val grid = Array[Array[Square]](Array(s1, s2))
    factory.createBoard(grid)
    s1.getSquareAt(Directions.SOUTH) should be (s2)
    s2.getSquareAt(Directions.SOUTH) should be (s1)
  }
}