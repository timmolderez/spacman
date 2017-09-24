package be.vub.spacman.npc.ghost


/**
  * A list of supported ghost colors.
  *
  * @author Jeroen Roosen
  */
object GhostColor extends Enumeration {
  type GhostColor = Value

  val
  RED, /** Shadow, a.k.a. Blinky. */
  CYAN, /** Bashful, a.k.a. Inky. */
  PINK, /** Speedy, a.k.a. Pinky. */
  ORANGE = Value /** Pokey, a.k.a. Clyde. */
}
