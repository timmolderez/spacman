package be.vub.spacman.sprite

import java.io.IOException
import java.util

import be.vub.spacman.PacmanConfigurationException
import be.vub.spacman.board.Directions
import be.vub.spacman.npc.ghost.GhostColor.GhostColor
import be.vub.spacman.sprite.nl.tudelft.jpacman.sprite.{AnimatedSprite, SpriteStore}

import scala.collection.AbstractMap

/**
  * Sprite Store containing the classic Pac-Man sprites.
  *
  * @author Jeroen Roosen
  */
object PacManSprites {

  /**
    * The sprite files are vertically stacked series for each direction, this
    * array denotes the order.
    */
  private val DIRECTIONS = Array(Directions.NORTH, Directions.EAST, Directions.SOUTH, Directions.WEST)

  /**
    * The image size in pixels.
    */
  private val SPRITE_SIZE = 16

  /**
    * The amount of frames in the pacman animation.
    */
  private val PACMAN_ANIMATION_FRAMES = 4

  /**
    * The amount of frames in the pacman dying animation.
    */
  private val PACMAN_DEATH_FRAMES = 11

  /**
    * The amount of frames in the ghost animation.
    */
  private val GHOST_ANIMATION_FRAMES = 2

  /**
    * The delay between frames.
    */
  private val ANIMATION_DELAY = 200
}

class PacManSprites extends SpriteStore {
  /**
    * @return A map of animated Pac-Man sprites for all directions.
    */
  def getPacmanSprites: Map[Directions.Direction, Sprite] = directionSprite("/sprite/pacman.png", PacManSprites.PACMAN_ANIMATION_FRAMES)

  /**
    * @return The animation of a dying Pac-Man.
    */
  def getPacManDeathAnimation: AnimatedSprite = {
    val resource = "/sprite/dead.png"
    val baseImage = loadSprite(resource)
    val animation = createAnimatedSprite(baseImage, PacManSprites.PACMAN_DEATH_FRAMES, PacManSprites.ANIMATION_DELAY, false)
    animation.setAnimating(false)
    animation
  }

  /**
    * Returns a new map with animations for all directions.
    *
    * @param resource
    * The resource name of the sprite.
    * @param frames
    * The number of frames in this sprite.
    * @return The animated sprite facing the given direction.
    */
  private def directionSprite(resource: String, frames: Int) : Map[Directions.Direction, Sprite]  = {
    val sprite = new scala.collection.mutable.HashMap[Directions.Direction, Sprite]
    val baseImage = loadSprite(resource)
    var i = 0
    while ( {
      i < PacManSprites.DIRECTIONS.length
    }) {
      val directionSprite = baseImage.split(0, i * PacManSprites.SPRITE_SIZE, frames * PacManSprites.SPRITE_SIZE, PacManSprites.SPRITE_SIZE)
      val animation = createAnimatedSprite(directionSprite, frames, PacManSprites.ANIMATION_DELAY, true)
      animation.setAnimating(true)
      sprite.put(PacManSprites.DIRECTIONS(i), animation)

      {
        i += 1; i - 1
      }
    }

    // Make the return value immutable
    sprite.toMap[Directions.Direction, Sprite]
  }

  /**
    * Returns a map of animated ghost sprites for all directions.
    *
    * @param color
    * The colour of the ghost.
    * @return The Sprite for the ghost.
    */
  def getGhostSprite(color: GhostColor): Map[Directions.Direction, Sprite] = {
    assert(color != null)
    val resource = "/sprite/ghost_" + color.toString.toLowerCase + ".png"
    directionSprite(resource, PacManSprites.GHOST_ANIMATION_FRAMES)
  }

  /**
    * @return The sprite for the wall.
    */
  def getWallSprite: Sprite = loadSprite("/sprite/wall.png")

  /**
    * @return The sprite for the ground.
    */
  def getGroundSprite: Sprite = loadSprite("/sprite/floor.png")

  /**
    * @return The sprite for the
    */
  def getPelletSprite: Sprite = loadSprite("/sprite/pellet.png")

  /**
    * Overloads the default sprite loading, ignoring the exception. This class
    * assumes all sprites are provided, hence the exception will be thrown as a
    * {@link RuntimeException}.
    *
    * {@inheritDoc }
    */
  override def loadSprite(resource: String): Sprite = try
    super.loadSprite(resource)
  catch {
    case e: IOException =>
      throw new PacmanConfigurationException("Unable to load sprite: " + resource, e)
  }
}