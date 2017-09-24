package be.vub.spacman.game

package nl.tudelft.jpacman.game

import java.util

import be.vub.spacman.level.Player
import be.vub.spacman.level.nl.tudelft.jpacman.level.Level
import com.google.common.collect.ImmutableList

/**
  * A game with one player and a single level.
  *
  * @author Jeroen Roosen
  */
class SinglePlayerGame protected(val player: Player, val level: Level) extends Game {
  assert(player != null)
  assert(level != null)
  level.registerPlayer(player)

  override def getPlayers: util.List[Player] = ImmutableList.of(player)

  override def getLevel: Level = level
}
