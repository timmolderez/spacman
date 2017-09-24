package be.vub.spacman.game

package nl.tudelft.jpacman.game

import java.util

import be.vub.spacman.board.Directions.Direction
import be.vub.spacman.level.Player
import be.vub.spacman.level.nl.tudelft.jpacman.level.Level

/**
  * A basic implementation of a Pac-Man game.
  *
  * @author Jeroen Roosen
  */
abstract class Game protected() extends Level.LevelObserver {
  inProgress = false
  /** <code>true</code> if the game is in progress. */
  private var inProgress = false
  /** Object that locks the start and stop methods. */
  final private val progressLock = new Object

  /**
    * Starts or resumes the game.
    */
  def start(): Unit = {
    progressLock.synchronized {
      if (isInProgress) return
      if (getLevel.isAnyPlayerAlive && getLevel.remainingPellets > 0) {
        inProgress = true
        getLevel.addObserver(this)
        getLevel.start
      }
    }
  }

  /**
    * Pauses the game.
    */
  def stop(): Unit = {
    progressLock.synchronized {
      if (!isInProgress) return
      inProgress = false
      getLevel.stop
    }
  }

  /**
    * @return <code>true</code> iff the game is started and in progress.
    */
  def isInProgress: Boolean = inProgress

  /**
    * @return An immutable list of the participants of this game.
    */
  def getPlayers: util.List[Player]

  /**
    * @return The level currently being played.
    */
  def getLevel: Level

  /**
    * Moves the specified player one square in the given direction.
    *
    * @param player
    * The player to move.
    * @param direction
    * The direction to move in.
    */
  def move(player: Player, direction: Direction): Unit = {
    if (isInProgress) { // execute player move.
      getLevel.move(player, direction)
    }
  }

  def levelWon(): Unit = {
    stop()
  }

  def levelLost(): Unit = {
    stop()
  }
}
