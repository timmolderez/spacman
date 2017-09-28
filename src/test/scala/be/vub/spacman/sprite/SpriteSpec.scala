package be.vub.spacman.sprite

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import java.io.IOException

import be.vub.spacman.{Launcher, UnitSpec}
import be.vub.spacman.sprite.AnimatedSprite
import be.vub.spacman.sprite.EmptySprite
import be.vub.spacman.sprite.Sprite
import be.vub.spacman.sprite.SpriteStore
import org.junit.Before
import org.junit.Test


/**
  * Verifies the loading of sprites.
  *
  * @author Jeroen Roosen
  */
object SpriteSpec {
  private val SPRITE_SIZE = 64
}

class SpriteSpec extends UnitSpec {
  private var sprite = null : Sprite
  private var store = null : SpriteStore

  /**
    * The common fixture of this test class is
    * a 64 by 64 pixel white sprite.
    *
    * @throws java.io.IOException
    * when the sprite could not be loaded.
    */
  override def withFixture(test: NoArgTest) = {
    // Test setup
    store = new SpriteStore
    sprite = store.loadSprite("/sprite/64x64white.png")

    // Run the test
    try test()

    // Test teardown
    finally {
      // Nothing to tear down..
    }
  }

  "Sprite width" should "be SPRITE_SIZE" in {
    sprite.getWidth should be (SpriteSpec.SPRITE_SIZE)
  }

  "Sprite height" should "be SPRITE_SIZE" in {
    sprite.getHeight should be (SpriteSpec.SPRITE_SIZE)
  }

  "Sprite with non-existing resource" should "throw IOException" in {
    a [IOException] should be thrownBy {
      store.loadSprite("/sprite/nonexistingresource.png")
    }
  }

  "Animation width" should "be cut correctly from its base image" in {
    val animation = store.createAnimatedSprite(sprite, 4, 0, false)
    animation.getWidth should be (16)
  }

  "Animation height" should "be cut correctly from its base image" in {
    val animation = store.createAnimatedSprite(sprite, 4, 0, false)
    animation.getHeight should be (64)
  }

  "A split sprite" should "be cut currectly from its base image (width)" in {
    val split = sprite.split(10, 11, 12, 13)
    split.getWidth should be (12)
  }

  "A split sprite" should "be cut currectly from its base image (height)" in {
    val split = sprite.split(10, 11, 12, 13)
    split.getHeight should be (13)
  }

  "An out of bounds split sprite" should "return an empty sprite" in {
    val split = sprite.split(10, 10, 64, 10)
    split.isInstanceOf[EmptySprite] should be (true)
  }
}
