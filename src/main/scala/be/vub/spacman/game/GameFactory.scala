package be.vub.spacman.game

package nl.tudelft.jpacman.game

import be.vub.spacman.game.nl.tudelft.jpacman.game.Game
import be.vub.spacman.game.nl.tudelft.jpacman.game.SinglePlayerGame
import be.vub.spacman.level.PlayerFactory
import be.vub.spacman.level.nl.tudelft.jpacman.level.Level


/**
  * Factory that provides Game objects.
  *
  * @author Jeroen Roosen
  */
class GameFactory(val playerFact: PlayerFactory)

/**
  * Creates a new game factory.
  *
  * @param playerFactory
  * The factory providing the player objects.
  */ {
  /**
    * Creates a game for a single level with one player.
    *
    * @param level
    * The level to create a game for.
    * @return A new single player game.
    */
  def createSinglePlayerGame(level: Level) = new SinglePlayerGame(playerFact.createPacMan, level)

  /**
    * Returns the player factory associated with this game factory.
    *
    * @return the player factory associated with this game factory.
    */
  protected def getPlayerFactory: PlayerFactory = playerFact
}