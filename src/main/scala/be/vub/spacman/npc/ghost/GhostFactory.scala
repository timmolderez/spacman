package be.vub.spacman.npc.ghost

import be.vub.spacman.sprite.PacManSprites

/**
  * Factory that creates ghosts.
  *
  * @author Jeroen Roosen
  */
class GhostFactory(val sprites: PacManSprites) {
  /**
    * Creates a new Blinky / Shadow, the red Ghost.
    *
    * @see Blinky
    * @return A new Blinky.
    */
  def createBlinky = new Blinky(sprites.getGhostSprite(GhostColor.RED))

  /**
    * Creates a new Pinky / Speedy, the pink Ghost.
    *
    * @see Pinky
    * @return A new Pinky.
    */
  def createPinky = new Pinky(sprites.getGhostSprite(GhostColor.PINK))

  /**
    * Creates a new Inky / Bashful, the cyan Ghost.
    *
    * @see Inky
    * @return A new Inky.
    */
  def createInky = new Inky(sprites.getGhostSprite(GhostColor.CYAN))

  /**
    * Creates a new Clyde / Pokey, the orange Ghost.
    *
    * @see Clyde
    * @return A new Clyde.
    */
  def createClyde = new Clyde(sprites.getGhostSprite(GhostColor.ORANGE))
}

