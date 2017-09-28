package be.vub.spacman.board

/**
  * Basic implementation of square.
  *
  * @author Jeroen Roosen
  */
class BasicSquare() extends Square {
  def isAccessibleTo(unit: BoardUnit) = true

  override def getSprite = null
}