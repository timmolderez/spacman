package be.vub.spacman.board

package nl.tudelft.jpacman.board

import be.vub.spacman.sprite.Sprite
import be.vub.spacman.board.Directions.Direction

/**
  * A unit that can be placed on a {@link Square}.
  *
  * @author Jeroen Roosen
  */
abstract class BoardUnit protected() {

  /**
    * The square this unit is currently occupying.
    */
  private var square = null : Square
  /**
    * The direction this unit is facing.
    */
  private var direction = Directions.EAST : Direction

  /**
    * Sets this unit to face the new direction.
    *
    * @param newDirection The new direction this unit is facing.
    */
  def setDirection(newDirection: Directions.Direction): Unit = this.direction = newDirection

  /**
    * Returns the current direction this unit is facing.
    *
    * @return The current direction this unit is facing.
    */
  def getDirection: Directions.Direction = this.direction

  /**
    * Returns the square this unit is currently occupying.
    *
    * @return The square this unit is currently occupying, or <code>null</code>
    *         if this unit is not on a square.
    */
  def getSquare: Square = {
    assert(invariant)
    square
  }

  /**
    * Occupies the target square iff this unit is allowed to as decided by
    * {@link Square#isAccessibleTo(Unit)}.
    *
    * @param target
    * The square to occupy.
    */
  def occupy(target: Square): Unit = {
    assert(target != null)
    if (square != null) square.remove(this)
    square = target
    target.put(this)
    assert(invariant)
  }

  /**
    * Leaves the currently occupying square, thus removing this unit from the board.
    */
  def leaveSquare(): Unit = {
    if (square != null) {
      square.remove(this)
      square = null
    }
    assert(invariant)
  }

  /**
    * Tests whether the square this unit is occupying has this unit listed as
    * one of its occupiers.
    *
    * @return <code>true</code> if the square this unit is occupying has this
    *         unit listed as one of its occupiers, or if this unit is currently
    *         not occupying any square.
    */
  protected def invariant: Boolean = square == null || square.getOccupants.contains(this)

  /**
    * Returns the sprite of this unit.
    *
    * @return The sprite of this unit.
    */
  def getSprite: Sprite
}
