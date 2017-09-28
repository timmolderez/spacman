package be.vub.spacman.level

import be.vub.spacman.UnitSpec
import be.vub.spacman.board.{Board, BoardFactory, Square}
import be.vub.spacman.npc.NPC
import be.vub.spacman.sprite.{AnimatedSprite, Sprite}
import com.google.common.collect.Lists
import org.scalamock.scalatest.MockFactory
import org.scalatest.OneInstancePerTest


/**
  * Tests various aspects of level.
  *
  * @author Jeroen Roosen
  */
// OneInstancePerTests is necessary to reuse the same mocks/stubs in multiple test cases!
class LevelSpec extends UnitSpec with MockFactory with OneInstancePerTest {
  /** The level under test. */
  private var level = null : Level
  /** An NPC on this level. */
  final private val ghost = stub[NPC]
  /** Starting position 1. */
  final private val square1 = stub[Square]
  /** Starting position 2. */
  final private val square2 = stub[Square]
  /** The board for this level. */
  final private val board = stub[MockableBoard]
  /** The collision map. */
  final private val collisions = stub[CollisionMap]

  /**
    * Sets up the level with the default board, a single NPC and a starting
    * square.
    */
  override def withFixture(test: NoArgTest) = {
    // Test setup
    val defaultInterval = 100L
    level = new Level(board, Lists.newArrayList(ghost), Lists.newArrayList(square1, square2), collisions)
    (ghost.getInterval _).when().returns(defaultInterval)

    // Run the test
    try test()
  }

  "Level" should "not be in progress if the game hasn't started yet" in {
    level.isInProgress should be (false)
  }

  it should "not be in progress when the game is stopped" in {
    level.stop()
    level.isInProgress should be (false)
  }

  it should "be in progress when the game is started" in {
    level.start
    level.isInProgress should be (true)
  }

  it should "not be in progress after starting and stopping" in {
    level.start
    level.stop
    level.isInProgress should be (false)
  }

  "Registering a player" should "put it in the correct starting square" in {
    val p = stub[MockablePlayer]
    level.registerPlayer(p)
    (p.occupy _).verify(square1)
  }

  "Registering a player twice" should "be OK" in {
    val p = stub[MockablePlayer]
    level.registerPlayer(p)
    level.registerPlayer(p)
    (p.occupy _).verify(square1)
  }

  "Registering a second player" should "also put that second player in the correct position" in {
    val p1 = stub[MockablePlayer]
    val p2 = stub[MockablePlayer]
    level.registerPlayer(p1)
    level.registerPlayer(p2)
    (p2.occupy _).verify(square2)
  }

  "Registering a third player" should "also put that third player in the correct position" in {
    val p1 = stub[MockablePlayer]
    val p2 = stub[MockablePlayer]
    val p3 = stub[MockablePlayer]
    level.registerPlayer(p1)
    level.registerPlayer(p2)
    level.registerPlayer(p3)
    (p3.occupy _).verify(square1)
  }
}

/** Workaround because directly mocking Board calls its default constructor with null arguments,
  * which is not allowed */
class MockableBoard extends Board(Array.ofDim(0,0)){}

/** Same thing here.. */
class MockablePlayer extends Player(null, new AnimatedSprite(new Array[Sprite](1), 0, false, false))