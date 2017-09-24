package be.vub.spacman.board

import be.vub.spacman.board.BoardFactory.{Ground, Wall}
import be.vub.spacman.sprite.{PacManSprites, Sprite}

/**
  * A factory that creates {@link Board} objects from 2-dimensional arrays of
  * {@link Square}s.
  *
  * @author Jeroen Roosen
  */
object BoardFactory {

  /**
    * A wall is a square that is inaccessible to anyone.
    *
    * @author Jeroen Roosen
    */
  final protected class Wall private[board](val background: Sprite) extends Square {
    def isAccessibleTo(unit: BoardUnit) = false

    override def getSprite: Sprite = background
  }

  /**
    * A wall is a square that is accessible to anyone.
    *
    * @author Jeroen Roosen
    */
  final protected class Ground private[board](val background: Sprite) extends Square {
    def isAccessibleTo(unit: BoardUnit) = true

    override def getSprite: Sprite = background
  }
}

class BoardFactory(val sprites: PacManSprites) {
  /**
    * Creates a new board from a grid of cells and connects it.
    *
    * @param grid
    * The square grid of cells, in which grid[x][y] corresponds to
    * the square at position x,y.
    * @return A new board, wrapping a grid of connected cells.
    */
  def createBoard(grid: Array[Array[Square]]): Board = {
    assert(grid != null)
    val board = new Board(grid)
    val width = board.getWidth
    val height = board.getHeight
    var x = 0
    while ( {
      x < width
    }) {
      var y = 0
      while ( {
        y < height
      }) {
        val square = grid(x)(y)
        import scala.collection.JavaConversions._
        for (dir <- Directions.values) {
          val dirX = (width + x + dir.dx) % width
          val dirY = (height + y + dir.dy) % height
          val neighbour = grid(dirX)(dirY)
          square.link(neighbour, dir)
        }

        {
          y += 1; y - 1
        }
      }

      {
        x += 1; x - 1
      }
    }
    board
  }

  /**
    * Creates a new square that can be occupied by any unit.
    *
    * @return A new square that can be occupied by any unit.
    */
  def createGround = new Ground(sprites.getGroundSprite)

  /**
    * Creates a new square that cannot be occupied by any unit.
    *
    * @return A new square that cannot be occupied by any unit.
    */
  def createWall = new Wall(sprites.getWallSprite)
}
