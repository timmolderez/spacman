package be.vub.spacman.npc.ghost

import be.vub.spacman.UnitSpec
import be.vub.spacman.board.{BoardFactory, BoardUnit, Directions}
import be.vub.spacman.board.Directions.Direction
import be.vub.spacman.level.{LevelFactory, MapParser}
import be.vub.spacman.sprite.PacManSprites
import com.google.common.collect.Lists
import org.scalamock.scalatest.MockFactory

/**
  * Tests the various methods provided by the {@link Navigation} class.
  *
  * @author Jeroen Roosen
  */
class NavigationSpec extends UnitSpec with MockFactory{
  /**
    * Map parser used to construct boards.
    */
  private var parser = null : MapParser

  override def withFixture(test: NoArgTest) = {
    // Test setup
    val sprites = new PacManSprites
    parser = new MapParser(new LevelFactory(sprites, new GhostFactory(sprites)), new BoardFactory(sprites))

    // Run the test
    try test()
  }

  "Path to square" should "be empty" in {
    val b = parser.parseMap(Lists.newArrayList(" ")).getBoard
    val s1 = b.squareAt(0, 0)
    val s2 = b.squareAt(0, 0)
    val path = Navigation.shortestPath(s1, s2, mock[BoardUnit])
  }

  "Path to the same square" should "be empty" in {
    val b = parser.parseMap(Lists.newArrayList(" ")).getBoard
    val s1 = b.squareAt(0, 0)
    val s2 = b.squareAt(0, 0)
    val path = Navigation.shortestPath(s1, s2, mock[BoardUnit])
    path.size() should be (0)
  }

  "A non-existant path" should "return null" in {
    val b = parser.parseMap(Lists.newArrayList("#####", "# # #", "#####")).getBoard
    val s1 = b.squareAt(1, 1)
    val s2 = b.squareAt(3, 1)
    val path = Navigation.shortestPath(s1, s2, mock[BoardUnit])
    path should be (null)
  }

  "Having no traveller" should "ignore terrain" in {
    val b = parser.parseMap(Lists.newArrayList("#####", "# # #", "#####")).getBoard
    val s1 = b.squareAt(1, 1)
    val s2 = b.squareAt(3, 1)
    val path = Navigation.shortestPath(s1, s2, null)
    path.toArray(new Array[Direction](2)) should be (Array(Directions.EAST, Directions.EAST))
  }

  "The algorithm" should "find a path in a straight line" in {
    val b = parser.parseMap(Lists.newArrayList("####", "#  #", "####")).getBoard
    val s1 = b.squareAt(1, 1)
    val s2 = b.squareAt(2, 1)
    val path = Navigation.shortestPath(s1, s2, mock[BoardUnit])
    path.toArray(new Array[Direction](1)) should be (Array(Directions.EAST))
  }

  it should "find a path when it has to take corners" in {
    val b = parser.parseMap(Lists.newArrayList("####", "#  #", "## #", "####")).getBoard
    val s1 = b.squareAt(1, 1)
    val s2 = b.squareAt(2, 2)
    val path = Navigation.shortestPath(s1, s2, mock[BoardUnit])
    path.toArray(new Array[Direction](2)) should be (Array(Directions.EAST, Directions.SOUTH))
  }

  "Nearest object" should "be detected" in {
    val b = parser.parseMap(Lists.newArrayList("#####", "# ..#", "#####")).getBoard
    val s1 = b.squareAt(1, 1)
    val s2 = b.squareAt(2, 1)
    val result = Navigation.findNearest(classOf[BoardUnit], s1).getSquare
    result should be (s2)
  }

  "If there is no nearest object, there" should "be no such location" in {
    val b = parser.parseMap(Lists.newArrayList(" ")).getBoard
    val s1 = b.squareAt(0, 0)
    val unit = Navigation.findNearest(classOf[BoardUnit], s1)
    unit should be (null)
  }

  "A ghost" should "be next to cell 1,1" in {
    try {
      val i = getClass.getResourceAsStream("/board.txt")
      try {
        val b = parser.parseMap(i).getBoard
        val s1 = b.squareAt(1, 1)
        val unit = Navigation.findNearest(classOf[Ghost], s1)
        unit should not be (null)
      } finally if (i != null) i.close()
    }
  }
}
