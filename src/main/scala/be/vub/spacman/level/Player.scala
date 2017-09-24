package be.vub.spacman.level

import be.vub.spacman.sprite.Sprite
import be.vub.spacman.board.Directions.Direction
import be.vub.spacman.board.nl.tudelft.jpacman.board.BoardUnit
import be.vub.spacman.sprite.nl.tudelft.jpacman.sprite.AnimatedSprite

/**
  * A player operated unit in our game.
  *
  * @author Jeroen Roosen
  */
class Player protected(/** The animations for every direction. */
                       val sprites: Map[Direction, Sprite],

                       /** The animation that is to be played when Pac-Man dies. */
                       val deathSprite: AnimatedSprite)
  extends BoardUnit {

  deathSprite.setAnimating(false)
  /** The amount of points accumulated by this player. */
  private var score = 0
  /** <code>true</code> iff this player is alive. */
  private var alive = true

  /**
    * Returns whether this player is alive or not.
    *
    * @return <code>true</code> iff the player is alive.
    */
  def isAlive: Boolean = alive

  /**
    * Sets whether this player is alive or not.
    *
    * @param isAlive
    * <code>true</code> iff this player is alive.
    */
  def setAlive(isAlive: Boolean): Unit = {
    if (isAlive) deathSprite.setAnimating(false)
    if (!isAlive) deathSprite.restart
    this.alive = isAlive
  }

  /**
    * Returns the amount of points accumulated by this player.
    *
    * @return The amount of points accumulated by this player.
    */
  def getScore: Int = score

  def getSprite: Sprite = {
    if (isAlive) return sprites(getDirection)
    deathSprite
  }

  /**
    * Adds points to the score of this player.
    *
    * @param points
    * The amount of points to add to the points this player already
    * has.
    */
  def addPoints(points: Int): Unit = {
    score += points
  }
}
