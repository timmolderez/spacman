package be.vub.spacman.npc.ghost

import be.vub.spacman.board.Directions.Direction
import be.vub.spacman.level.Player
import be.vub.spacman.sprite.Sprite

/**
  * <p>
  * An implementation of the classic Pac-Man ghost Shadow.
  * </p>
  * <p>
  * Nickname: Blinky. As his name implies, Shadow is usually a constant shadow on
  * Pac-Man's tail. When he's not patrolling the top-right corner of the maze,
  * Shadow tries to find the quickest route to Pac-Man's position. Despite the
  * fact that Pinky's real name is Speedy, Shadow is actually the fastest of the
  * ghosts because of when there are only a few pellets left, Blinky drastically
  * speeds up, which can make him quite deadly. In the original Japanese version,
  * his name is Oikake/Akabei.
  * </p>
  * <p>
  * <b>AI:</b> When the ghosts are not patrolling in their home corners (Blinky:
  * top-right, Pinky: top-left, Inky: bottom-right, Clyde: bottom-left), Blinky
  * will attempt to shorten the distance between Pac-Man and himself. If he has
  * to choose between shortening the horizontal or vertical distance, he will
  * choose to shorten whichever is greatest. For example, if Pac-Man is four grid
  * spaces to the left, and seven grid spaces above Blinky, he'll try to move up
  * towards Pac-Man before he moves to the left.
  * </p>
  * <p>
  * Source: http://strategywiki.org/wiki/Pac-Man/Getting_Started
  * </p>
  *
  * @author Jeroen Roosen
  *
  */
object Blinky {
  /**
    * The variation in intervals, this makes the ghosts look more dynamic and
    * less predictable.
    */
  private val INTERVAL_VARIATION = 50
  /**
    * The base movement interval.
    */
  private val MOVE_INTERVAL = 250
}

class Blinky(val spriteMap: Map[Direction, Sprite])
  extends Ghost(spriteMap, Blinky.MOVE_INTERVAL, Blinky.INTERVAL_VARIATION) {

  /**
    * {@inheritDoc }
    *
    * <p>
    * When the ghosts are not patrolling in their home corners (Blinky:
    * top-right, Pinky: top-left, Inky: bottom-right, Clyde: bottom-left),
    * Blinky will attempt to shorten the distance between Pac-Man and himself.
    * If he has to choose between shortening the horizontal or vertical
    * distance, he will choose to shorten whichever is greatest. For example,
    * if Pac-Man is four grid spaces to the left, and seven grid spaces above
    * Blinky, he'll try to move up towards Pac-Man before he moves to the left.
    * </p>
    */
  override def nextMove: Direction = {
    // TODO Blinky should patrol his corner every once in a while
    // TODO Implement his actual behaviour instead of simply chasing.
    val target = Navigation.findNearest(classOf[Player], getSquare).getSquare
    if (target == null) return randomMove
    val path = Navigation.shortestPath(getSquare, target, this)
    if (path != null && !path.isEmpty) return path.get(0)
    randomMove
  }
}
