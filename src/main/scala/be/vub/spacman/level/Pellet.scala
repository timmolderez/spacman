package be.vub.spacman.level

import be.vub.spacman.board.nl.tudelft.jpacman.board.BoardUnit
import be.vub.spacman.sprite.Sprite

/**
  * A pellet, one of the little dots Pac-Man has to collect.
  *
  * @author Jeroen Roosen
  */
class Pellet(val value: Int, val image: Sprite) extends BoardUnit {
  /**
    * Returns the point value of this pellet.
    *
    * @return The point value of this pellet.
    */
  def getValue: Int = value

  def getSprite: Sprite = image
}
