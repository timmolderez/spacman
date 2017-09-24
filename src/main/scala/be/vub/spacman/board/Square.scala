package be.vub.spacman.board

import java.util

import be.vub.spacman.board.Directions.Direction
import be.vub.spacman.sprite.Sprite
import com.google.common.collect.ImmutableList

/**
  * A square on a {@link Board}, which can (or cannot, depending on the type) be
  * occupied by units.
  *
  * @author Jeroen Roosen
  */
abstract class Square protected() {

  /**
    * The units occupying this square, in order of appearance.
    */
  final private var occupants = new util.ArrayList[BoardUnit] : util.List[BoardUnit]

  /**
    * The collection of squares adjacent to this square.
    */
  final private var neighbours = new util.HashMap[Direction, Square]()

  assert(invariant)

  /**
    * Returns the square adjacent to this square.
    *
    * @param direction
    * The direction of the adjacent square.
    * @return The adjacent square in the given direction.
    */
  def getSquareAt(direction: Direction): Square = neighbours.get(direction)

  /**
    * Links this square to a neighbour in the given direction. Note that this
    * is a one-way connection.
    *
    * @param neighbour
    * The neighbour to link.
    * @param direction
    * The direction the new neighbour is in, as seen from this cell.
    */
  def link(neighbour: Square, direction: Direction): Unit = {
    neighbours.put(direction, neighbour)
    assert(invariant)
  }

  /**
    * Returns an immutable list of units occupying this square, in the order in
    * which they occupied this square (i.e. oldest first.)
    *
    * @return An immutable list of units occupying this square, in the order in
    *         which they occupied this square (i.e. oldest first.)
    */
  def getOccupants: util.List[BoardUnit] = ImmutableList.copyOf(occupants.iterator())

  /**
    * Adds a new occupant to this square.
    *
    * @param occupant
    * The unit to occupy this square.
    */
  private[board] def put(occupant: BoardUnit): Unit = {
    assert(occupant != null)
    assert(!occupants.contains(occupant))
    occupants.add(occupant)
  }

  /**
    * Removes the unit from this square if it was present.
    *
    * @param occupant
    * The unit to be removed from this square.
    */
  private[board] def remove(occupant: BoardUnit): Unit = {
    assert(occupant != null)
    occupants.remove(occupant)
  }

  /**
    * Verifies that all occupants on this square have indeed listed this square
    * as the square they are currently occupying.
    *
    * @return <code>true</code> iff all occupants of this square have this
    *         square listed as the square they are currently occupying.
    */
  final protected def invariant: Boolean = {
    import scala.collection.JavaConversions._
    for (occupant <- occupants) {
      if (occupant.getSquare ne this) return false
    }
    true
  }

  /**
    * Determines whether the unit is allowed to occupy this square.
    *
    * @param unit
    * The unit to grant or deny access.
    * @return <code>true</code> iff the unit is allowed to occupy this square.
    */
  def isAccessibleTo(unit: BoardUnit): Boolean

  /**
    * Returns the sprite of this square.
    *
    * @return The sprite of this square.
    */
  def getSprite: Sprite
}
