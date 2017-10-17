package be.vub.spacman

import java.awt.event.KeyEvent
import java.io.{File, IOException}

import be.vub.spacman.board.{BoardFactory, Directions}
import be.vub.spacman.level.{Level, LevelFactory, MapParser, PlayerFactory}
import be.vub.spacman.sprite.PacManSprites
import be.vub.spacman.board.Directions.Direction
import be.vub.spacman.game.{Game, GameFactory}
import be.vub.spacman.npc.ghost.GhostFactory
import be.vub.spacman.ui.{Action, PacmanUI, PacmanUiBuilder}


/**
  * Creates and launches the JPacMan UI.
  * (Modified so you can specify which level .txt file to load - Tim Molderez)
  *
  * @author Jeroen Roosen
  */
object Launcher {
  private val SPRITE_STORE = new PacManSprites

  /**
    * Main execution method for the Launcher.
    *
    * @param args
    * The command line arguments - which are ignored.
    * @throws IOException
    * When a resource could not be read.
    */
  @throws[IOException]
  def main(args: Array[String]): Unit = {
    new Launcher().launch()
  }
}

class Launcher {
  private var pacManUI = null : PacmanUI
  private var game = null : Game

  /**
    * @return The game object this launcher will start when { @link #launch()}
    *                                                               is called.
    */
  def getGame: Game = game

  def f(): Int = {
    5
  } ensuring( (x: Int) => true)

  /**
    * Creates a new game.
    *
    * @param boardFile Text file specifying the Pacman level
    * @return a new Game.
    */
  def makeGame(boardFile: String): Game = {
    require(boardFile != null)
    require(resourceExists(boardFile))

    val gf = getGameFactory
    val level = makeLevel(boardFile)
    gf.createSinglePlayerGame(level)
  } ensuring(!_.isInProgress)

  private def resourceExists(file: String): Boolean = {
    classOf[Launcher].getResourceAsStream("/" + file) != null
  }

  /**
    * Creates a new level. By default this method will use the map parser to
    * parse the default board stored in the <code>board.txt</code> resource.
    *
    * @return A new level.
    */
  def makeLevel(boardFile: String): Level = {
    val parser = getMapParser
    val boardStream = classOf[Launcher].getResourceAsStream("/" + boardFile)
    try {
      parser.parseMap(boardStream)
    } catch {
      case e: IOException =>
        throw new PacmanConfigurationException("Unable to create level.", e)
    } finally if (boardStream != null) boardStream.close()
  }

  /**
    * @return A new map parser object using the factories from
    *         { @link #getLevelFactory()} and { @link #getBoardFactory()}.
    */
  protected def getMapParser = new MapParser(getLevelFactory, getBoardFactory)

  /**
    * @return A new board factory using the sprite store from
    *         { @link #getSpriteStore()}.
    */
  protected def getBoardFactory = new BoardFactory(getSpriteStore)

  /**
    * @return The default { @link PacManSprites}.
    */
  protected def getSpriteStore: PacManSprites = Launcher.SPRITE_STORE

  /**
    * @return A new factory using the sprites from { @link #getSpriteStore()}
    *                                                      and the ghosts from { @link #getGhostFactory()}.
    */
  protected def getLevelFactory = new LevelFactory(getSpriteStore, getGhostFactory)

  /**
    * @return A new factory using the sprites from { @link #getSpriteStore()}.
    */
  protected def getGhostFactory = new GhostFactory(getSpriteStore)

  /**
    * @return A new factory using the players from { @link #getPlayerFactory()}.
    */
  protected def getGameFactory = new GameFactory(getPlayerFactory)

  protected def getPlayerFactory = new PlayerFactory(getSpriteStore)

  /**
    * Adds key events UP, DOWN, LEFT and RIGHT to a game.
    *
    * @param builder
    * The { @link PacmanUiBuilder} that will provide the UI.
    * @param game
    * The game that will process the events.
    */
  protected def addSinglePlayerKeys(builder: PacmanUiBuilder, game: Game): Unit = {
    val p1 = getSinglePlayer(game)
    builder.addKey(KeyEvent.VK_UP, new Action() {
      def doAction(): Unit = {
        game.move(p1, Directions.NORTH)
      }
    }).addKey(KeyEvent.VK_DOWN, new Action() {
      def doAction(): Unit = {
        game.move(p1, Directions.SOUTH)
      }
    }).addKey(KeyEvent.VK_LEFT, new Action() {
      def doAction(): Unit = {
        game.move(p1, Directions.WEST)
      }
    }).addKey(KeyEvent.VK_RIGHT, new Action() {
      def doAction(): Unit = {
        game.move(p1, Directions.EAST)
      }
    })
  }

  private def getSinglePlayer(game: Game) = {
    val players = game.getPlayers
    if (players.isEmpty) throw new IllegalArgumentException("Game has 0 players.")
    val p1 = players.get(0)
    p1
  }

  /**
    * Creates and starts a JPac-Man game.
    */
  def launch(): Unit = {
    launch("board.txt")
  }

  /**
    * Creates and starts a JPac-Man game.
    *
    * @param boardFile Text file (in resources directory) specifying the Pacman level
    */
  def launch(boardFile: String): Unit = {
    require(boardFile != null)
    game = makeGame(boardFile)
    val builder = new PacmanUiBuilder().withDefaultButtons
    addSinglePlayerKeys(builder, game)
    pacManUI = builder.build(game)
    pacManUI.start
  }

  /**
    * Disposes of the UI. For more information see {@link javax.swing.JFrame#dispose()}.
    */
  def dispose(): Unit = {
    pacManUI.dispose
  }
}
