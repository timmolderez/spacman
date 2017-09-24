package be.vub.spacman.ui

import java.awt.GridLayout
import java.util
import javax.swing.{JLabel, JPanel, SwingConstants}

import be.vub.spacman.level.Player


/**
  * A panel consisting of a column for each player, with the numbered players on
  * top and their respective scores underneath.
  *
  * @author Jeroen Roosen
  *
  */
@SerialVersionUID(1L)
object ScorePanel {
  /**
    * The default way in which the score is shown.
    */
  val DEFAULT_SCORE_FORMATTER = new ScorePanel.ScoreFormatter() {
    override def format(p: Player): String = String.format("Score: %3d", p.getScore.toString)
  }

  /**
    * Provide means to format the score for a given player.
    */
  trait ScoreFormatter {
    /**
      * Format the score of a given player.
      *
      * @param p The player and its score
      * @return Formatted score.
      */
    def format(p: Player): String
  }

}

@SerialVersionUID(1L)
class ScorePanel(val players: util.List[Player]) extends JPanel {
  assert(players != null)
  setLayout(new GridLayout(2, players.size))
  var i = 1
  while ( {
    i <= players.size
  }) {
    add(new JLabel("Player " + i, SwingConstants.CENTER))

    {
      i += 1; i - 1
    }
  }

  import scala.collection.JavaConversions._

  for (p <- players) {
    val scoreLabel = new JLabel("0", SwingConstants.CENTER)
    scoreLabels.put(p, scoreLabel)
    add(scoreLabel)
  }
  /**
    * The map of players and the labels their scores are on.
    */
  final private var scoreLabels = new util.LinkedHashMap[Player, JLabel]
  /**
    * The way to format the score information.
    */
  private var scoreFormatter = ScorePanel.DEFAULT_SCORE_FORMATTER

  /**
    * Refreshes the scores of the players.
    */
  def refresh(): Unit = {
    import scala.collection.JavaConversions._
    for (entry <- scoreLabels.entrySet) {
      val p = entry.getKey
      var score = ""
      if (!p.isAlive) score = "You died. "
      score += scoreFormatter.format(p)
      entry.getValue.setText(score)
    }
  }

  /**
    * Let the score panel use a dedicated score formatter.
    *
    * @param sf Score formatter to be used.
    */
  def setScoreFormatter(sf: ScorePanel.ScoreFormatter): Unit = {
    assert(sf != null)
    scoreFormatter = sf
  }
}
