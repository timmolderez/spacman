package be.vub.spacman.npc.ghost

package nl.tudelft.jpacman.npc.ghost

import java.util
import java.util.Random

import be.vub.spacman.board.Directions
import be.vub.spacman.board.Directions.Direction
import be.vub.spacman.npc.NPC
import be.vub.spacman.sprite.Sprite

/**
  * An antagonist in the game of Pac-Man, a ghost.
  *
  * @author Jeroen Roosen
  */
abstract class Ghost protected(
                                val sprites: Map[Direction, Sprite],/** The sprite map, one sprite for each direction. */
                                val moveInterval: Int, /** The base move interval of the ghost. */
                                val intervalVariation: Int) /** The random variation added to the {@link #moveInterval}. */
  extends NPC {
  def getSprite: Option[Sprite] = sprites.get(getDirection)

  def getInterval: Long = this.moveInterval + new Random().nextInt(this.intervalVariation)

  /**
    * Determines a possible move in a random direction.
    *
    * @return A direction in which the ghost can move, or <code>null</code> if
    *         the ghost is shut in by inaccessible squares.
    */
  protected def randomMove: Direction = {
    val square = getSquare
    val directions = new util.ArrayList[Direction]
    for (d <- Directions.values) {
      if (square.getSquareAt(d).isAccessibleTo(this)) directions.add(d)
    }
    if (directions.isEmpty) return null
    val i = new Random().nextInt(directions.size)
    directions.get(i)
  }
}
