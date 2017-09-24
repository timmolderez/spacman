package be.vub.spacman.ui

package nl.tudelft.jpacman.ui

/**
  * An action that can be executed.
  *
  * @author Jeroen Roosen
  */
trait Action {
  /**
    * Executes the action.
    */
  def doAction(): Unit
}