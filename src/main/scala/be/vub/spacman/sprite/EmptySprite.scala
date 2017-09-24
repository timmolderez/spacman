package be.vub.spacman.sprite

import be.vub.spacman.sprite.Sprite
import java.awt.Graphics


/**
  * Empty Sprite which does not contain any data. When this sprite is drawn,
  * nothing happens.
  *
  * @author Jeroen Roosen
  */
class EmptySprite extends Sprite {
  override def draw(g: Graphics, x: Int, y: Int, width: Int, height: Int): Unit = {
    // nothing to draw.
  }

  override def split(x: Int, y: Int, width: Int, height: Int) = new EmptySprite

  override def getWidth = 0

  override def getHeight = 0
}
