package be.vub.spacman.level

package nl.tudelft.jpacman.level

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util

import be.vub.spacman.PacmanConfigurationException
import be.vub.spacman.board.nl.tudelft.jpacman.board.{BoardFactory, Square}
import be.vub.spacman.level.nl.tudelft.jpacman.level.LevelFactory
import be.vub.spacman.npc.NPC


/**
  * Creates new {@link Level}s from text representations.
  *
  * @author Jeroen Roosen
  */
class MapParser(/**
                  * The factory that creates the levels.
                  */
                val levelCreator: LevelFactory,

                /**
                  * The factory that creates the squares and board.
                  */
                val boardCreator: BoardFactory) {
  /**
    * Parses the text representation of the board into an actual level.
    *
    * <ul>
    * <li>Supported characters:
    * <li>' ' (space) an empty square.
    * <li>'#' (bracket) a wall.
    * <li>'.' (period) a square with a pellet.
    * <li>'P' (capital P) a starting square for players.
    * <li>'G' (capital G) a square with a ghost.
    * </ul>
    *
    * @param map
    * The text representation of the board, with map[x][y]
    * representing the square at position x,y.
    * @return The level as represented by this text.
    */
  def parseMap(map: Array[Array[Char]]): Level = {
    val width = map.length
    val height = map(0).length
    val grid = Array.ofDim[Square](width, height)
    val ghosts = new util.ArrayList[NPC]
    val startPositions = new util.ArrayList[Square]
    makeGrid(map, width, height, grid, ghosts, startPositions)
    val board = boardCreator.createBoard(grid)
    levelCreator.createLevel(board, ghosts, startPositions)
  }

  private def makeGrid(map: Array[Array[Char]], width: Int, height: Int, grid: Array[Array[Square]], ghosts: util.List[NPC], startPositions: util.List[Square]): Unit = {
    var x = 0
    while ( {
      x < width
    }) {
      var y = 0
      while ( {
        y < height
      }) {
        val c = map(x)(y)
        addSquare(grid, ghosts, startPositions, x, y, c)

        {
          y += 1; y - 1
        }
      }

      {
        x += 1; x - 1
      }
    }
  }

  private def addSquare(grid: Array[Array[Square]], ghosts: util.List[NPC], startPositions: util.List[Square], x: Int, y: Int, c: Char): Unit = {
    c match {
      case ' ' =>
        grid(x)(y) = boardCreator.createGround
      case '#' =>
        grid(x)(y) = boardCreator.createWall
      case '.' =>
        val pelletSquare = boardCreator.createGround
        grid(x)(y) = pelletSquare
        levelCreator.createPellet.occupy(pelletSquare)
      case 'G' =>
        val ghostSquare = makeGhostSquare(ghosts)
        grid(x)(y) = ghostSquare
      case 'P' =>
        val playerSquare = boardCreator.createGround
        grid(x)(y) = playerSquare
        startPositions.add(playerSquare)
      case _ =>
        throw new PacmanConfigurationException("Invalid character at " + x + "," + y + ": " + c)
    }
  }

  private def makeGhostSquare(ghosts: util.List[NPC]) = {
    val ghostSquare = boardCreator.createGround
    val ghost = levelCreator.createGhost
    ghosts.add(ghost)
    ghost.occupy(ghostSquare)
    ghostSquare
  }

  /**
    * Parses the list of strings into a 2-dimensional character array and
    * passes it on to {@link #parseMap(char[][])}.
    *
    * @param text
    * The plain text, with every entry in the list being a equally
    * sized row of squares on the board and the first element being
    * the top row.
    * @return The level as represented by the text.
    * @throws PacmanConfigurationException If text lines are not properly formatted.
    */
  def parseMap(text: util.List[String]): Level = {
    checkMapFormat(text)
    val height = text.size
    val width = text.get(0).length
    val map = Array.ofDim[Char](width, height)
    var x = 0
    while ( {
      x < width
    }) {
      var y = 0
      while ( {
        y < height
      }) {
        map(x)(y) = text.get(y).charAt(x)

        {
          y += 1; y - 1
        }
      }

      {
        x += 1; x - 1
      }
    }
    parseMap(map)
  }

  /**
    * Check the correctness of the map lines in the text.
    *
    * @param text Map to be checked
    * @throws PacmanConfigurationException if map is not OK.
    */
  private def checkMapFormat(text: util.List[String]): Unit = {
    if (text == null) throw new PacmanConfigurationException("Input text cannot be null.")
    if (text.isEmpty) throw new PacmanConfigurationException("Input text must consist of at least 1 row.")
    val width = text.get(0).length
    if (width == 0) throw new PacmanConfigurationException("Input text lines cannot be empty.")
    import scala.collection.JavaConversions._
    for (line <- text) {
      if (line.length != width) throw new PacmanConfigurationException("Input text lines are not of equal width.")
    }
  }

  /**
    * Parses the provided input stream as a character stream and passes it
    * result to {@link #parseMap(List)}.
    *
    * @param source
    * The input stream that will be read.
    * @return The parsed level as represented by the text on the input stream.
    * @throws IOException
    * when the source could not be read.
    */
  @throws[IOException]
  def parseMap(source: InputStream): Level = try {
    val reader = new BufferedReader(new InputStreamReader(source, "UTF-8"))
    try {
      val lines = new util.ArrayList[String]
      while ( {
        reader.ready
      }) lines.add(reader.readLine)
      parseMap(lines)
    } finally if (reader != null) reader.close()
  }
}
