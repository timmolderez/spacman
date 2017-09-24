package be.vub.spacman.level

import be.vub.spacman.board.nl.tudelft.jpacman.board.BoardUnit

/**
  * A table containing all (relevant) collisions between different types of
  * units.
  *
  * @author Jeroen Roosen
  */
trait CollisionMap {
  /**
    * Collides the two units and handles the result of the collision, which may
    * be nothing at all.
    * @param collider
    *          The unit that causes the collision by occupying a square with
    *          another unit already on it.
    * @param collidee
    *          The unit that is already on the square that is being invaded.
    */
  def collide[C1 <: BoardUnit, C2 <: BoardUnit](collider: C1, collidee: C2): Unit
}
