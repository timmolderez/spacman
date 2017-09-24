package be.vub.spacman.npc.ghost

import java.util

import be.vub.spacman.board.Directions
import be.vub.spacman.board.Directions.Direction
import be.vub.spacman.level.Player
import be.vub.spacman.sprite.Sprite


/**
  * <p>
  * An implementation of the classic Pac-Man ghost Pokey.
  * </p>
  * <p>
  * Nickname: Clyde. Pokey needs a new nickname because out of all the ghosts,
  * Pokey is the least likely to "C'lyde" with Pac-Man. Pokey is always the last
  * ghost out of the regenerator, and the loner of the gang, usually off doing
  * his own thing when not patrolling the bottom-left corner of the maze. His
  * behavior is very random, so while he's not likely to be following you in hot
  * pursuit with the other ghosts, he is a little less predictable, and still a
  * danger. In Japan, his name is Otoboke/Guzuta.
  * </p>
  * <p>
  * <b>AI:</b> Pokey has two basic AIs, one for when he's far from Pac-Man, and
  * one for when he is near to Pac-Man. When the ghosts are not patrolling their
  * home corners, and Pokey is far away from Pac-Man (beyond eight grid spaces),
  * Pokey behaves very much like Blinky, trying to move to Pac-Man's exact
  * location. However, when Pokey gets within eight grid spaces of Pac-Man, he
  * automatically changes his behavior and goes to patrol his home corner in the
  * bottom-left section of the maze.
  * </p>
  * <p>
  * Source: http://strategywiki.org/wiki/Pac-Man/Getting_Started
  * </p>
  *
  * @author Jeroen Roosen
  *
  */
object Clyde {
  /**
    * The amount of cells Clyde wants to stay away from Pac Man.
    */
  private val SHYNESS = 8
  /**
    * The variation in intervals, this makes the ghosts look more dynamic and
    * less predictable.
    */
  private val INTERVAL_VARIATION = 50
  /**
    * The base movement interval.
    */
  private val MOVE_INTERVAL = 250
  /**
    * A map of opposite directions.
    */
  private val OPPOSITES = new util.HashMap[Direction, Direction]

  OPPOSITES.put(Directions.NORTH, Directions.SOUTH)
  OPPOSITES.put(Directions.SOUTH, Directions.NORTH)
  OPPOSITES.put(Directions.WEST, Directions.EAST)
  OPPOSITES.put(Directions.EAST, Directions.WEST)

}

class Clyde(val spriteMap: Map[Direction, Sprite])
  extends Ghost(spriteMap, Clyde.MOVE_INTERVAL, Clyde.INTERVAL_VARIATION) {

  /**
    * {@inheritDoc }
    *
    * <p>
    * Pokey has two basic AIs, one for when he's far from Pac-Man, and one for
    * when he is near to Pac-Man. When the ghosts are not patrolling their home
    * corners, and Pokey is far away from Pac-Man (beyond eight grid spaces),
    * Pokey behaves very much like Blinky, trying to move to Pac-Man's exact
    * location. However, when Pokey gets within eight grid spaces of Pac-Man,
    * he automatically changes his behavior and goes to patrol his home corner
    * in the bottom-left section of the maze.
    * </p>
    * <p>
    * <b>Implementation:</b> Lacking a patrol function so far, Clyde will just
    * move in the opposite direction when he gets within 8 cells of Pac-Man.
    * </p>
    */
  override def nextMove: Direction = {
    val target = Navigation.findNearest(classOf[Player], getSquare).getSquare
    if (target == null) return randomMove
    val path = Navigation.shortestPath(getSquare, target, this)
    if (path != null && !path.isEmpty) {
      val d = path.get(0)
      if (path.size <= Clyde.SHYNESS) return Clyde.OPPOSITES.get(d)
      return d
    }
    randomMove
  }
}
