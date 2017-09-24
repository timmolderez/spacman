package be.vub.spacman.level

import be.vub.spacman.sprite.PacManSprites

/**
  * Factory that creates Players.
  *
  * @author Jeroen Roosen
  */
class PlayerFactory(val sprites: PacManSprites) {

  /**
    * Creates a new player with the classic Pac-Man sprites.
    *
    * @return A new player.
    */
  def createPacMan = new Player(getSprites.getPacmanSprites, getSprites.getPacManDeathAnimation)

  /**
    * The sprites created by the factory.
    *
    * @return The sprites for the player created.
    */
  protected def getSprites: PacManSprites = sprites
}

