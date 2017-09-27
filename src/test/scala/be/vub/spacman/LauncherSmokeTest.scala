package be.vub.spacman

package nl.tudelft.jpacman

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import be.vub.spacman.board.Directions
import be.vub.spacman.game.Game
import be.vub.spacman.level.Player
import org.junit.After
import org.junit.Before
import org.junit.Test


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
@SuppressWarnings(Array("magicnumber")) object LauncherSmokeTest {
  /**
    * Make number of moves in given direction.
    *
    * @param game     The game we're playing
    * @param dir      The direction to be taken
    * @param numSteps The number of steps to take
    */
  def move(game: Game, dir: Directions.Direction, numSteps: Int): Unit = {
    val player = game.getPlayers.get(0)
    var i = 0
    while ( {
      i < numSteps
    }) {
      game.move(player, dir)

      {
        i += 1; i - 1
      }
    }
  }
}

class LauncherSmokeTest {
  private var launcher = null : Launcher

  /**
    * Launch the user interface.
    */
  @Before def setUpPacman(): Unit = {
    launcher = new Launcher
    launcher.launch
  }

  /**
    * Quit the user interface when we're done.
    */
  @After def tearDown(): Unit = {
    launcher.dispose
  }

  /**
    * Launch the game, and imitate what would happen in a typical game.
    * The test is only a smoke test, and not a focused small test.
    * Therefore it is OK that the method is a bit too long.
    *
    * @throws InterruptedException Since we're sleeping in this test.
    */
  @Test
  @throws[InterruptedException]
  def smokeTest(): Unit = {
    val game = launcher.getGame
    val player = game.getPlayers.get(0)
    // start cleanly.
    assertFalse(game.isInProgress)
    game.start
    assertTrue(game.isInProgress)
    assertEquals(0, player.getScore)
    // get points
    game.move(player, Directions.EAST)
    assertEquals(10, player.getScore)
    // now moving back does not change the score
    game.move(player, Directions.WEST)
    assertEquals(10, player.getScore)
    // try to move as far as we can
    LauncherSmokeTest.move(game, Directions.EAST, 7)
    assertEquals(60, player.getScore)
    // move towards the monsters
    LauncherSmokeTest.move(game, Directions.NORTH, 6)
    assertEquals(120, player.getScore)
    // no more points to earn here.
    LauncherSmokeTest.move(game, Directions.WEST, 2)
    assertEquals(120, player.getScore)
    LauncherSmokeTest.move(game, Directions.NORTH, 2)
    // Sleeping in tests is generally a bad idea.
    // Here we do it just to let the monsters move.
    Thread.sleep(3000L)
    // we're close to monsters, this will get us killed.
    LauncherSmokeTest.move(game, Directions.WEST, 10)
    LauncherSmokeTest.move(game, Directions.EAST, 10)
    assertFalse(player.isAlive)
    game.stop
    assertFalse(game.isInProgress)
  }
}

