package be.vub.spacman.level

import be.vub.spacman.board.nl.tudelft.jpacman.board.BoardUnit
import be.vub.spacman.npc.ghost.nl.tudelft.jpacman.npc.ghost.Ghost


/**
  * An extensible default interaction map for collisions caused by the player.
  *
  * The implementation makes use of the interactionmap, and as such can be easily
  * and declaratively extended when new types of units (ghosts, players, ...) are
  * added.
  *
  * @author Arie van Deursen
  * @author Jeroen Roosen
  *
  */
object DefaultPlayerInteractionMap {
  /**
    * Creates the default collisions Player-Ghost and Player-Pellet.
    *
    * @return The collision map containing collisions for Player-Ghost and
    *         Player-Pellet.
    */
  private def defaultCollisions = {
    val collisionMap = new CollisionInteractionMap
    collisionMap.onCollision(classOf[Player], classOf[Ghost],
      new CollisionInteractionMap.CollisionHandler[Player, Ghost] {
      def handleCollision(player: Player, ghost: Ghost): Unit = {
        player.setAlive(false)
      }
    })
    collisionMap.onCollision(classOf[Player], classOf[Pellet], new Nothing() {
      def handleCollision(player: Player, pellet: Pellet): Unit = {
        pellet.leaveSquare()
        player.addPoints(pellet.getValue)
      }
    })
    collisionMap
  }
}

class DefaultPlayerInteractionMap extends CollisionMap {
  final private val collisions = DefaultPlayerInteractionMap.defaultCollisions

  override def collide[C1 <: BoardUnit, C2 <: BoardUnit](mover: C1, movedInto: C2): Unit= {
    collisions.collide(mover, movedInto)
  }
}
