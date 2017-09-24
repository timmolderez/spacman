package be.vub.spacman.npc

import be.vub.spacman.board.Directions
import be.vub.spacman.board.nl.tudelft.jpacman.board.BoardUnit

/**
  * A non-player unit.
  *
  * @author Jeroen Roosen
  */
abstract class NPC extends BoardUnit {
  /**
    * The time that should be taken between moves.
    *
    * @return The suggested delay between moves in milliseconds.
    */
  def getInterval: Long

  /**
    * Calculates the next move for this unit and returns the direction to move
    * in.
    *
    * @return The direction to move in, or <code>null</code> if no move could
    *         be devised.
    */
  def nextMove: Directions.Direction
}
