package be.vub.spacman.npc.ghost

package nl.tudelft.jpacman.npc.ghost

import be.vub.spacman.board.Directions.Direction
import be.vub.spacman.level.Player
import be.vub.spacman.sprite.Sprite


/**
  * <p>
  * An implementation of the classic Pac-Man ghost Speedy.
  * </p>
  * <p>
  * Nickname: Pinky. Speedy gets his name for an unusual reason. Speedy appears
  * to try to outsmart Pac-Man and crash into Pac-Man from the opposite
  * direction. The truth behind this is that when Speedy isn't patrolling the
  * top-left corner of the maze, he tries to attack Pac-Man by moving to where he
  * is going to be (that is, a few spaces ahead of Pac-Man's current direction)
  * instead of right where he is, as Blinky does. It's difficult to use this to
  * your advantage, but it's possible. If Pinky is coming at you and you face a
  * different direction, even briefly, he may just turn away and attempt to cut
  * you off in the new direction while you return to your original direction. In
  * the original Japanese version, his name is Machibuse/Pinky.
  * </p>
  * <p>
  * <b>AI:</b> When the ghosts are not patrolling their home corners, Pinky wants
  * to go to the place that is four grid spaces ahead of Pac-Man in the direction
  * that Pac-Man is facing. If Pac-Man is facing down, Pinky wants to go to the
  * location exactly four spaces below Pac-Man. Moving towards this place uses
  * the same logic that Blinky uses to find Pac-Man's exact location. Pinky is
  * affected by a targeting bug if Pac-Man is facing up - when he moves or faces
  * up, Pinky tries moving towards a point up, and left, four spaces.
  * </p>
  * <p>
  * <i>Note: In the original arcade series, the ghosts' genders are unspecified
  * and assumed to be male. In 1999, the USA division of Namco and Namco Hometech
  * developed the Pac-Man World series and declared Pinky to be female.</i>
  * </p>
  * <p>
  * Source: http://strategywiki.org/wiki/Pac-Man/Getting_Started
  * </p>
  *
  * @author Jeroen Roosen
  *
  */
object Pinky {
  private val SQUARES_AHEAD = 4
  /**
    * The variation in intervals, this makes the ghosts look more dynamic and
    * less predictable.
    */
  private val INTERVAL_VARIATION = 50
  /**
    * The base movement interval.
    */
  private val MOVE_INTERVAL = 200
}

class Pinky(val spriteMap: Map[Direction, Sprite])
  extends Ghost(spriteMap, Pinky.MOVE_INTERVAL, Pinky.INTERVAL_VARIATION) {
  /**
    * {@inheritDoc }
    *
    * <p>
    * When the ghosts are not patrolling their home corners, Pinky wants to go
    * to the place that is four grid spaces ahead of Pac-Man in the direction
    * that Pac-Man is facing. If Pac-Man is facing down, Pinky wants to go to
    * the location exactly four spaces below Pac-Man. Moving towards this place
    * uses the same logic that Blinky uses to find Pac-Man's exact location.
    * Pinky is affected by a targeting bug if Pac-Man is facing up - when he
    * moves or faces up, Pinky tries moving towards a point up, and left, four
    * spaces.
    * </p>
    */
  override def nextMove: Direction = {
    val player = Navigation.findNearest(classOf[Player], getSquare)
    if (player == null) return randomMove
    val targetDirection = player.getDirection
    var destination = player.getSquare
    var i = 0
    while ( {
      i < Pinky.SQUARES_AHEAD
    }) {
      destination = destination.getSquareAt(targetDirection)

      {
        i += 1; i - 1
      }
    }
    val path = Navigation.shortestPath(getSquare, destination, this)
    if (path != null && !path.isEmpty) return path.get(0)
    randomMove
  }
}
