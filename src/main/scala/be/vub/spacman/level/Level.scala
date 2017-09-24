package be.vub.spacman.level

import java.util
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

import be.vub.spacman.board.{Board, BoardUnit, Square}
import be.vub.spacman.npc.NPC
import be.vub.spacman.board.Directions.Direction

import scala.collection.mutable


/**
  * A level of Pac-Man. A level consists of the board with the players and the
  * AIs on it.
  *
  * @author Jeroen Roosen
  */
@SuppressWarnings(Array("PMD.TooManyMethods")) object Level {

  /**
    * An observer that will be notified when the level is won or lost.
    *
    * @author Jeroen Roosen
    */
  trait LevelObserver {
    /**
      * The level has been won. Typically the level should be stopped when
      * this event is received.
      */
    def levelWon(): Unit

    /**
      * The level has been lost. Typically the level should be stopped when
      * this event is received.
      */
    def levelLost(): Unit
  }

}

@SuppressWarnings(Array("PMD.TooManyMethods")) class Level(/** The board of this level. */
                                                           val board: Board, val ghosts: util.List[NPC],
                                                           /** The squares from which players can start this game. */
                                                           val startSquares: util.List[Square],

                                                           /** The table of possible collisions between units. */
                                                           val collisions: CollisionMap)
 {
  assert(board != null)
  assert(ghosts != null)
  assert(startSquares != null)
  this.inProgress = false

  import scala.collection.JavaConversions._

  for (g <- ghosts) {
    npcs.put(g, null)
  }

  /** The lock that ensures moves are executed sequential. */
  final private val moveLock = new Object
  /** The lock that ensures starting and stopping can't interfere with each other. */
  final private val startStopLock = new Object
  /** The NPCs of this level and, if they are running, their schedules. */
  final private var npcs = new mutable.HashMap[NPC, ScheduledExecutorService]
  /** <code>rue</code> iff this level is currently in progress, i.e. players and NPCs can move.  */
  private var inProgress = false
  /** The start current selected starting square. */
  private var startSquareIndex = 0
  /** The players on this level. */
  final private var players = new util.ArrayList[Player]
  /** The objects observing this level. */
  final private var observers = new util.HashSet[Level.LevelObserver]

  /**
    * Adds an observer that will be notified when the level is won or lost.
    *
    * @param observer
    * The observer that will be notified.
    */
  def addObserver(observer: Level.LevelObserver): Unit = {
    observers.add(observer)
  }

  /**
    * Removes an observer if it was listed.
    *
    * @param observer
    * The observer to be removed.
    */
  def removeObserver(observer: Level.LevelObserver): Unit = {
    observers.remove(observer)
  }

  /**
    * Registers a player on this level, assigning him to a starting position. A
    * player can only be registered once, registering a player again will have
    * no effect.
    *
    * @param p
    * The player to register.
    */
  def registerPlayer(p: Player): Unit = {
    assert(p != null)
    assert(!startSquares.isEmpty)
    if (players.contains(p)) return
    players.add(p)
    val square = startSquares.get(startSquareIndex)
    p.occupy(square)
    startSquareIndex += 1
    startSquareIndex %= startSquares.size
  }

  /**
    * Returns the board of this level.
    *
    * @return The board of this level.
    */
  def getBoard: Board = board

  /**
    * Moves the unit into the given direction if possible and handles all
    * collisions.
    *
    * @param unit
    * The unit to move.
    * @param direction
    * The direction to move the unit in.
    */
  def move(unit: BoardUnit, direction: Direction): Unit = {
    assert(unit != null)
    assert(direction != null)
    if (!isInProgress) return

    moveLock.synchronized {
      unit.setDirection(direction)
      val location = unit.getSquare
      val destination = location.getSquareAt(direction)
      if (destination.isAccessibleTo(unit)) {
        val occupants = destination.getOccupants
        unit.occupy(destination)
        import scala.collection.JavaConversions._
        for (occupant <- occupants) {
          collisions.collide(unit, occupant)
        }
      }
      updateObservers()
    }
  }

  /**
    * Starts or resumes this level, allowing movement and (re)starting the
    * NPCs.
    */
  def start(): Unit = {
    startStopLock.synchronized {
      if (isInProgress) return
      startNPCs()
      inProgress = true
      updateObservers()
    }
  }

  /**
    * Stops or pauses this level, no longer allowing any movement on the board
    * and stopping all NPCs.
    */
  def stop(): Unit = {
    startStopLock.synchronized {
      if (!isInProgress) return
      stopNPCs()
      inProgress = false
    }
  }

  /**
    * Starts all NPC movement scheduling.
    */
  private def startNPCs(): Unit = {
    for (npc <- npcs.keySet) {
      val service = Executors.newSingleThreadScheduledExecutor
      service.schedule(new NpcMoveTask(service, npc), npc.getInterval / 2, TimeUnit.MILLISECONDS)
      npcs.put(npc, service)
    }
  }

  /**
    * Stops all NPC movement scheduling and interrupts any movements being
    * executed.
    */
  private def stopNPCs(): Unit = {
    import scala.collection.JavaConversions._
    for (e <- npcs.entrySet) {
      e.getValue.shutdownNow
    }
  }

  /**
    * Returns whether this level is in progress, i.e. whether moves can be made
    * on the board.
    *
    * @return <code>true</code> iff this level is in progress.
    */
  def isInProgress: Boolean = inProgress

  /**
    * Updates the observers about the state of this level.
    */
  private def updateObservers(): Unit = {
    if (!isAnyPlayerAlive) {
      import scala.collection.JavaConversions._
      for (o <- observers) {
        o.levelLost()
      }
    }
    if (remainingPellets == 0) {
      import scala.collection.JavaConversions._
      for (o <- observers) {
        o.levelWon()
      }
    }
  }

  /**
    * Returns <code>true</code> iff at least one of the players in this level
    * is alive.
    *
    * @return <code>true</code> if at least one of the registered players is
    *         alive.
    */
  def isAnyPlayerAlive: Boolean = {
    import scala.collection.JavaConversions._
    for (p <- players) {
      if (p.isAlive) return true
    }
    false
  }

  /**
    * Counts the pellets remaining on the board.
    *
    * @return The amount of pellets remaining on the board.
    */
  def remainingPellets: Int = {
    val b = getBoard
    var pellets = 0
    var x = 0
    while ( {
      x < b.getWidth
    }) {
      var y = 0
      while ( {
        y < b.getHeight
      }) {
        import scala.collection.JavaConversions._
        for (u <- b.squareAt(x, y).getOccupants) {
          if (u.isInstanceOf[Pellet]) pellets += 1
        }

        {
          y += 1; y - 1
        }
      }

      {
        x += 1; x - 1
      }
    }
    assert(pellets >= 0)
    pellets
  }

  /**
    * A task that moves an NPC and reschedules itself after it finished.
    *
    * @author Jeroen Roosen
    */
  final private class NpcMoveTask private[level](/**
                                                   * The service executing the task.
                                                   */
                                                 val service: ScheduledExecutorService,

                                                 /**
                                                   * The NPC to move.
                                                   */
                                                 val npc: NPC)
    extends Runnable {

    override def run(): Unit = {
      val nextMove = npc.nextMove
      if (nextMove != null) move(npc, nextMove)
      val interval = npc.getInterval
      service.schedule(this, interval, TimeUnit.MILLISECONDS)
    }
  }

}
