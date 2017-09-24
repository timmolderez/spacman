package be.vub.spacman.game

import be.vub.spacman.level.PlayerFactory
import be.vub.spacman.level.Level


/**
  * Factory that provides Game objects.
  *
  * @author Jeroen Roosen
  */
class GameFactory(val playerFact: PlayerFactory) {
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