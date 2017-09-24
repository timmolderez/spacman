package be.vub.spacman.board

/**
  * An enumeration of possible directions on a two-dimensional square grid.
  *
  * @author Jeroen Roosen
  */
object Directions {

  sealed abstract class Direction(val dx: Int, val dy: Int) {}

  case object NORTH extends Direction(0, -1)
  case object SOUTH extends Direction(0, 1)
  case object WEST extends Direction(-1, 0)
  case object EAST extends Direction(1, 0)

  val values = Set(NORTH, SOUTH, WEST, EAST)
}