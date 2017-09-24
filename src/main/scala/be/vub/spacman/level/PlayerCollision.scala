package be.vub.spacman.level

import be.vub.spacman.board.BoardUnit
import be.vub.spacman.npc.ghost.Ghost

/**
  * A simple implementation of a collision map for the JPacman player.
  * <p>
  * It uses a number of instanceof checks to implement the multiple dispatch for the
  * collisionmap. For more realistic collision maps, this approach will not scale,
  * and the recommended approach is to use a {@link CollisionInteractionMap}.
  *
  * @author Arie van Deursen, 2014
  *
  */
class PlayerCollisions extends CollisionMap {

  override def collide[C1 <: BoardUnit, C2 <: BoardUnit](mover: C1, collidedOn: C2): Unit = {
    if (mover.isInstanceOf[Player]) playerColliding(mover.asInstanceOf[Player], collidedOn)
    else if (mover.isInstanceOf[Ghost]) ghostColliding(mover.asInstanceOf[Ghost], collidedOn)
    else if (mover.isInstanceOf[Pellet]) pelletColliding(mover.asInstanceOf[Pellet], collidedOn)
  }

  private def playerColliding(player: Player, collidedOn: BoardUnit): Unit = {
    if (collidedOn.isInstanceOf[Ghost]) playerVersusGhost(player, collidedOn.asInstanceOf[Ghost])
    if (collidedOn.isInstanceOf[Pellet]) playerVersusPellet(player, collidedOn.asInstanceOf[Pellet])
  }

  private def ghostColliding(ghost: Ghost, collidedOn: BoardUnit): Unit = {
    if (collidedOn.isInstanceOf[Player]) playerVersusGhost(collidedOn.asInstanceOf[Player], ghost)
  }

  private def pelletColliding(pellet: Pellet, collidedOn: BoardUnit): Unit = {
    if (collidedOn.isInstanceOf[Player]) playerVersusPellet(collidedOn.asInstanceOf[Player], pellet)
  }

  /**
    * Actual case of player bumping into ghost or vice versa.
    *
    * @param player The player involved in the collision.
    * @param ghost  The ghost involved in the collision.
    */
  def playerVersusGhost(player: Player, ghost: Ghost): Unit = {
    player.setAlive(false)
  }

  /**
    * Actual case of player consuming a pellet.
    *
    * @param player The player involved in the collision.
    * @param pellet The pellet involved in the collision.
    */
  def playerVersusPellet(player: Player, pellet: Pellet): Unit = {
    pellet.leaveSquare()
    player.addPoints(pellet.getValue)
  }
}
