package be.vub.spacman.sprite

package nl.tudelft.jpacman.sprite

import be.vub.spacman.sprite.EmptySprite
import be.vub.spacman.sprite.Sprite
import java.awt.Graphics


/**
  * Animated sprite, renders the frame depending on the time of requesting the
  * draw.
  *
  * @author Jeroen Roosen
  */
object AnimatedSprite {
  /**
    * Static empty sprite to serve as the end of a non-looping sprite.
    */
  private val END_OF_LOOP = new EmptySprite : Sprite
}

class AnimatedSprite(
                      val frames: Array[Sprite], /** frames of the animation */
                      val animationDelay: Int, /** delay between frames */
                      val looping: Boolean, /** is this a looping animation? */
                      var animating: Boolean) /** is the animation animating from the start?  */
  extends Sprite {
  assert(frames.length > 0)

  /**
    * The animation itself, in frames.
    */
  final private var animationFrames = frames.clone()

  /**
    * The index of the current frame.
    */
  private var current = 0

  /**
    * The {@link System#currentTimeMillis()} stamp of the last update.
    */
  private var lastUpdate = System.currentTimeMillis

  /**
    * Creates a new animating sprite that will change frames every interval. By
    * default the sprite is not animating.
    *
    * @param frames
    * The frames of this animation.
    * @param delay
    * The delay between frames.
    * @param loop
    * Whether or not this sprite should be looping.
    */
  def this(frames: Array[Sprite], delay: Int, loop: Boolean) {
    this(frames, delay, loop, false)
  }

  /**
    * @return The frame of the current index.
    */
  private def currentSprite = {
    var result = AnimatedSprite.END_OF_LOOP
    if (current < animationFrames.length) result = animationFrames(current)
    assert(result != null)
    result
  }

  /**
    * Starts or stops the animation of this sprite.
    *
    * @param isAnimating
    * <code>true</code> to animate this sprite or <code>false</code>
    * to stop animating this sprite.
    */
  def setAnimating(isAnimating: Boolean): Unit = {
    this.animating = isAnimating
  }

  /**
    * (Re)starts the current animation.
    */
  def restart(): Unit = {
    this.current = 0
    this.lastUpdate = System.currentTimeMillis
    setAnimating(true)
  }

  override def draw(g: Graphics, x: Int, y: Int, width: Int, height: Int): Unit = {
    update()
    currentSprite.draw(g, x, y, width, height)
  }

  override def split(x: Int, y: Int, width: Int, height: Int): Sprite = {
    update()
    currentSprite.split(x, y, width, height)
  }

  /**
    * Updates the current frame index depending on the current system time.
    */
  private def update(): Unit = {
    val now = System.currentTimeMillis
    if (animating) while ( {
      lastUpdate < now
    }) {
      lastUpdate += animationDelay
      current += 1
      if (looping) current %= animationFrames.length
      else if (current == animationFrames.length) animating = false
    }
    else lastUpdate = now
  }

  override def getWidth: Int = {
    assert(currentSprite != null)
    currentSprite.getWidth
  }

  override def getHeight: Int = {
    assert(currentSprite != null)
    currentSprite.getHeight
  }
}
