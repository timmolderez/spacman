package be.vub.spacman.npc.ghost

package nl.tudelft.jpacman.npc.ghost

import java.util

import be.vub.spacman.board.Directions
import be.vub.spacman.board.nl.tudelft.jpacman.board.{BoardUnit, Square}
import be.vub.spacman.board.Directions.Direction

/**
  * Navigation provides utility to nagivate on {@link Square}s.
  *
  * @author Jeroen Roosen
  */
object Navigation {

  /**
    * Calculates the shortest path. This is done by BFS. This search ensures
    * the traveller is allowed to occupy the squares on the way, or returns the
    * shortest path to the square regardless of terrain if no traveller is
    * specified.
    *
    * @param from
    * The starting square.
    * @param to
    * The destination.
    * @param traveller
    * The traveller attempting to reach the destination. If
    * traveller is set to <code>null</code>, this method will ignore
    * terrain and find the shortest path whether it can actually be
    * reached or not.
    * @return The shortest path to the destination or <code>null</code> if no
    *         such path could be found. When the destination is the current
    *         square, an empty list is returned.
    */
  def shortestPath(from: Square, to: Square, traveller: BoardUnit): util.List[Direction] = {
    if (from.equals(to)) return new util.ArrayList[Direction]
    val targets = new util.ArrayList[Navigation.Node]
    val visited = new util.HashSet[Square]
    targets.add(new Navigation.Node(null, from, null))
    while ( {
      !targets.isEmpty
    }) {
      val n = targets.remove(0)
      val s = n.getSquare
      if (s.equals(to)) return n.getPath
      visited.add(s)
      addNewTargets(traveller, targets, visited, n, s)
    }
    null
  }

  private def addNewTargets(traveller: BoardUnit, targets: util.List[Navigation.Node], visited: util.Set[Square], n: Navigation.Node, s: Square): Unit = {
    import scala.collection.JavaConversions._
    for (d <- Directions.values) {
      val target = s.getSquareAt(d)
      if (!visited.contains(target) && (traveller == null || target.isAccessibleTo(traveller))) targets.add(new Navigation.Node(d, target, n))
    }
  }

  /**
    * Finds the nearest unit of the given type and returns its location. This
    * method will perform a breadth first search starting from the given
    * square.
    *
    * @param type
    * The type of unit to search for.
    * @param currentLocation
    * The starting location for the search.
    * @return The nearest unit of the given type, or <code>null</code> if no
    *         such unit could be found.
    */
  def findNearest(`type`: Class[_ <: BoardUnit], currentLocation: Square): BoardUnit = {
    val toDo = new util.ArrayList[Square]
    val visited = new util.HashSet[Square]
    toDo.add(currentLocation)
    while ( {
      !toDo.isEmpty
    }) {
      val square = toDo.remove(0)
      val unit = findUnit(`type`, square)
      if (unit != null) return unit
      visited.add(square)
      import scala.collection.JavaConversions._
      for (d <- Directions.values) {
        val newTarget = square.getSquareAt(d)
        if (!visited.contains(newTarget) && !toDo.contains(newTarget)) toDo.add(newTarget)
      }
    }
    null
  }

  /**
    * Determines whether a square has an occupant of a certain type.
    *
    * @param type
    * The type to search for.
    * @param square
    * The square to search.
    * @return A unit of type T, iff such a unit occupies this square, or
    *         <code>null</code> of none does.
    */
  def findUnit(`type`: Class[_ <: BoardUnit], square: Square): BoardUnit = {
    import scala.collection.JavaConversions._
    for (u <- square.getOccupants) {
      if (`type`.isInstance(u)) return u
    }
    null
  }

  /**
    * Helper class to keep track of the path.
    *
    * @author Jeroen Roosen
    */
  final private class Node(/** The direction for this node, which is <code>null</code> for the root node. */
                           val direction: Direction,
                           /** The square associated with this node. */
                           val square: Square,
                           /** The parent node, which is <code>null</code> for the root node. */
                           val parent: Navigation.Node)
{
    /**
      * @return The direction for this node, or <code>null</code> if this
      *         node is a root node.
      */
    def getDirection = direction

    /**
      * @return The square for this node.
      */
    def getSquare = square

    /**
      * @return The parent node, or <code>null</code> if this node is a root
      *         node.
      */
    private def getParent = parent

    /**
      * Returns the list of values from the root of the tree to this node.
      *
      * @return The list of values from the root of the tree to this node.
      */
    def getPath: util.List[Direction] = {
      if (getParent == null) return new util.ArrayList[Direction]
      val path = parent.getPath
      path.add(getDirection)
      path
    }
  }

}

final class Navigation private() {
}
