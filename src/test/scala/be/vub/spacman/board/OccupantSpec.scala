package be.vub.spacman.board

import be.vub.spacman.UnitSpec

/**
  * Test suite to confirm that {@link Unit}s correctly (de)occupy squares.
  *
  * @author Jeroen Roosen
  *
  */
class OccupantSpec extends UnitSpec {
  /**
    * The unit under test.
    */
  private var unit = null : BasicUnit

  /**
    * Setup a board that can be used for testing.
    */
  override def withFixture(test: NoArgTest) = {
    // Test setup
    unit = new BasicUnit

    // Run the test
    try test()
  }


  "A unit" should "initially have no contents" in {
    unit.getSquare should be (null)
  }

  "A unit" should "contain a target square" in {
    val target = new BasicSquare
    unit.occupy(target)
    unit.getSquare should be (target)
    target.getOccupants should contain (unit)
  }

  "A unit" should "contain a target square after adding it twice" in {
    val target = new BasicSquare
    unit.occupy(target)
    unit.occupy(target)
    unit.getSquare should be (target)
    target.getOccupants should contain (unit)
  }
}
