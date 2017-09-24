package be.vub.spacman.level

package nl.tudelft.jpacman.level

import java.util

import be.vub.spacman.board.Board
import be.vub.spacman.level.CollisionMap
import be.vub.spacman.level.Pellet
import be.vub.spacman.level.PlayerCollisions
import be.vub.spacman.board.Directions.Direction
import be.vub.spacman.board.nl.tudelft.jpacman.board.Square
import be.vub.spacman.npc.NPC
import be.vub.spacman.npc.ghost.{GhostColor, GhostFactory}
import be.vub.spacman.npc.ghost.nl.tudelft.jpacman.npc.ghost.Ghost
import be.vub.spacman.sprite.{PacManSprites, Sprite}


/**
  * Factory that creates levels and units.
  *
  * @author Jeroen Roosen
  */
object LevelFactory {
  private val GHOSTS = 4
  private val BLINKY = 0
  private val INKY = 1
  private val PINKY = 2
  private val CLYDE = 3
  /**
    * The default value of a pellet.
    */
  private val PELLET_VALUE = 10

  /**
    * Implementation of an NPC that wanders around randomly.
    *
    * @author Jeroen Roosen
    */
  private object RandomGhost {
    /**
      * The suggested delay between moves.
      */
    private val DELAY = 175L
  }

  final private class RandomGhost private[level](val ghostSprite: Map[Direction, Sprite])
    extends Ghost(ghostSprite, RandomGhost.DELAY.toInt, 0) {
    def nextMove: Direction = randomMove
  }

}

class LevelFactory(/**
                     * The sprite store that provides sprites for units.
                     */
                   val sprites: PacManSprites,

                   /**
                     * The factory providing ghosts.
                     */
                   val ghostFact: GhostFactory) {
  this.ghostIndex = -1
  /**
    * Used to cycle through the various ghost types.
    */
  private var ghostIndex = 0

  /**
    * Creates a new level from the provided data.
    *
    * @param board
    * The board with all ghosts and pellets occupying their squares.
    * @param ghosts
    * A list of all ghosts on the board.
    * @param startPositions
    * A list of squares from which players may start the game.
    * @return A new level for the board.
    */
  def createLevel(board: Board, ghosts: util.List[NPC], startPositions: util.List[Square]): Level = { // We'll adopt the simple collision map for now.
    val collisionMap = new PlayerCollisions
    new Level(board, ghosts, startPositions, collisionMap)
  }

  /**
    * Creates a new ghost.
    *
    * @return The new ghost.
    */
  private[level] def createGhost = {
    ghostIndex += 1
    ghostIndex %= LevelFactory.GHOSTS
    ghostIndex match {
      case LevelFactory.BLINKY =>
        ghostFact.createBlinky
      case LevelFactory.INKY =>
        ghostFact.createInky
      case LevelFactory.PINKY =>
        ghostFact.createPinky
      case LevelFactory.CLYDE =>
        ghostFact.createClyde
      case _ =>
        new LevelFactory.RandomGhost(sprites.getGhostSprite(GhostColor.RED))
    }
  }

  /**
    * Creates a new pellet.
    *
    * @return The new pellet.
    */
  def createPellet = new Pellet(LevelFactory.PELLET_VALUE, sprites.getPelletSprite)
}

