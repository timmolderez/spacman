package be.vub.spacman.sprite

import java.awt.Graphics
import java.awt.GraphicsEnvironment
import java.awt.Image
import java.awt.Transparency
import java.awt.image.BufferedImage


/**
  * Basic implementation of a Sprite, it merely consists of a static image.
  *
  * @author Jeroen Roosen
  */
class ImageSprite(val image: Image) extends Sprite {
  def draw(g: Graphics, x: Int, y: Int, width: Int, height: Int): Unit = {
    g.drawImage(image, x, y, x + width, y + height, 0, 0, image.getWidth(null), image.getHeight(null), null)
  }

  override def split(x: Int, y: Int, width: Int, height: Int): Sprite = {
    if (withinImage(x, y) && withinImage(x + width - 1, y + height - 1)) {
      val newImg = newImage(width, height)
      newImg.createGraphics.drawImage(image, 0, 0, width, height, x, y, x + width, y + height, null)
      return new ImageSprite(newImg)
    }
    new EmptySprite
  }

  private def withinImage(x: Int, y: Int) = x < image.getWidth(null) && x >= 0 && y < image.getHeight(null) && y >= 0

  /**
    * Creates a new, empty image of the given width and height. Its
    * transparency will be a bitmask, so no try ARGB image.
    *
    * @param width
    * The width of the new image.
    * @param height
    * The height of the new image.
    * @return The new, empty image.
    */
  private def newImage(width: Int, height: Int) : BufferedImage = {
    val gc = GraphicsEnvironment.getLocalGraphicsEnvironment.getDefaultScreenDevice.getDefaultConfiguration
    gc.createCompatibleImage(width, height, Transparency.BITMASK)
  }

  override def getWidth: Int = image.getWidth(null)

  override def getHeight: Int = image.getHeight(null)
}
