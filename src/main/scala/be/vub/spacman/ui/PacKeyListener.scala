package be.vub.spacman.ui

import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.util

/**
  * A key listener based on a set of keyCode-action pairs.
  *
  * @author Jeroen Roosen
  */
class PacKeyListener private[ui](/**
                                   * The mappings of keyCode to action.
                                   */
                                 val mappings: Map[Int, Action]) extends KeyListener {
  assert(mappings != null)

  override def keyPressed(e: KeyEvent): Unit = {
    assert(e != null)
    val action = mappings.get(e.getKeyCode).get
    if (action != null) action.doAction
  }

  override def keyTyped(e: KeyEvent): Unit = {
    // do nothing
  }

  override def keyReleased(e: KeyEvent): Unit = {
  }
}