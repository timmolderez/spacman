package be.vub.spacman

import be.vub.spacman.board.Directions
import be.vub.spacman.nl.tudelft.jpacman.LauncherSmokeTest

/**
  * Smoke test launching the full game,
  * and attempting to make a number of typical moves.
  *
  * This is <strong>not</strong> a <em>unit</em> test -- it is an end-to-end test
  * trying to execute a large portion of the system's behavior directly from the
  * user interface. It uses the actual sprites and monster AI, and hence
  * has little control over what is happening in the game.
  *
  * Because it is an end-to-end test, it is somewhat longer
  * and has more assert statements than what would be good
  * for a small and focused <em>unit</em> test.
  *
  * @author Arie van Deursen, March 2014.
  */
class LauncherSmokeSpec extends UnitSpec {

  private var launcher = null : Launcher

  override def withFixture(test: NoArgTest) = {
    // Test setup
    launcher = new Launcher
    launcher.launch

    // Run the test
    try test()

    // Test teardown
    finally {
      launcher.dispose
    }
  }

  "The player" should "be dead" in {

    val game = launcher.getGame
    val player = game.getPlayers.get(0)
    // start cleanly.
    game.isInProgress should be (false)
    game.start
    game.isInProgress should be (true)
    player.getScore should be (0)
    // get points
    game.move(player, Directions.EAST)
    player.getScore should be (10)
    // now moving back does not change the score
    game.move(player, Directions.WEST)
    player.getScore should be (10)
    // try to move as far as we can
    LauncherSmokeTest.move(game, Directions.EAST, 7)
    player.getScore should be (60)
    // move towards the monsters
    LauncherSmokeTest.move(game, Directions.NORTH, 6)
    player.getScore should be (120)
    // no more points to earn here.
    LauncherSmokeTest.move(game, Directions.WEST, 2)
    player.getScore should be (120)
    LauncherSmokeTest.move(game, Directions.NORTH, 2)
    // Sleeping in tests is generally a bad idea.
    // Here we do it just to let the monsters move.
    Thread.sleep(3000L)
    // we're close to monsters, this will get us killed.
    LauncherSmokeTest.move(game, Directions.WEST, 10)
    LauncherSmokeTest.move(game, Directions.EAST, 10)
    player.isAlive should be (false)
    game.stop
    game.isInProgress should be (false)
  }

}
