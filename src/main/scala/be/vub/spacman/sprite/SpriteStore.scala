package be.vub.spacman.sprite

import java.awt.image.BufferedImage
import java.io.IOException
import java.io.InputStream
import java.util
import javax.imageio.ImageIO


/**
  * Utility to load {@link Sprite}s.
  *
  * @author Jeroen Roosen
  */
class SpriteStore() {

  /**
    * We only need to load images once, so we keep track
    * of them in a hash map.
    */
  final private val spriteMap = new util.HashMap[String, Sprite]

  /**
    * Loads a sprite from a resource on the class path.
    * Sprites are loaded once, and then stored in the store
    * so that they can be efficiently retrieved.
    *
    * @param resource
    * The resource path.
    * @return The sprite for the resource.
    * @throws IOException
    * When the resource could not be loaded.
    */
  @throws[IOException]
  def loadSprite(resource: String): Sprite = {
    var result = spriteMap.get(resource)
    if (result == null) {
      result = loadSpriteFromResource(resource)
      spriteMap.put(resource, result)
    }
    result
  }

  /**
    * Loads a sprite from a resource on the class path.
    *
    * @param resource
    * The resource path.
    * @return A new sprite for the resource.
    * @throws IOException
    * When the resource could not be loaded.
    */
  @throws[IOException]
  private def loadSpriteFromResource(resource: String) = {
    val input = classOf[SpriteStore].getResourceAsStream(resource)
    try {
      if (input == null) throw new IOException("Unable to load " + resource + ", resource does not exist.")
      val image = ImageIO.read(input)
      new ImageSprite(image)
    } finally if (input != null) input.close()
  }

  /**
    * Creates a new {@link AnimatedSprite} from a base image.
    *
    * @param baseImage
    * The base image to convert into an animation.
    * @param frames
    * The amount of frames of the animation.
    * @param delay
    * The delay between frames.
    * @param loop
    * Whether this sprite is a looping animation or not.
    * @return The animated sprite.
    */
  def createAnimatedSprite(baseImage: Sprite, frames: Int, delay: Int, loop: Boolean): AnimatedSprite = {
    assert(baseImage != null)
    assert(frames > 0)
    val frameWidth = baseImage.getWidth / frames
    val animation = new Array[Sprite](frames)
    var i = 0
    while ( {
      i < frames
    }) {
      animation(i) = baseImage.split(i * frameWidth, 0, frameWidth, baseImage.getHeight)

      {
        i += 1; i - 1
      }
    }
    new AnimatedSprite(animation, delay, loop)
  }
}

