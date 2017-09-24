package be.vub.spacman.ui

package nl.tudelft.jpacman.ui

import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.util
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel

/**
  * A panel containing a button for every registered action.
  *
  * @author Jeroen Roosen
  */
@SerialVersionUID(1L)
class ButtonPanel private[ui](val buttons: Map[String, Action], val parent: JFrame) extends JPanel {
  assert(buttons != null)
  assert(parent != null)

  import scala.collection.JavaConversions._

  for (caption <- buttons.keySet) {
    val button = new JButton(caption)
    button.addActionListener(new ActionListener() {
      override def actionPerformed(e: ActionEvent): Unit = {
        buttons.get(caption).get.doAction()
        parent.requestFocusInWindow
      }
    })
    add(button)
  }
}