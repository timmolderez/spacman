package be.vub.spacman.board

/**
  * A top-down view of a matrix of {@link Square}s.
  *
  * @author Jeroen Roosen
  */
class Board(val board: Array[Array[Square]]) {

  assert(board != null)
  assert(invariant, "Initial grid cannot contain null squares")

  /**
    * Whatever happens, the squares on the board can't be null.
    *
    * @return false if any square on the board is null.
    */
  final protected def invariant: Boolean = {
    for (row <- board) {
      for (square <- row) {
        if (square == null) return false
      }
    }
    true
  }

  /**
    * Returns the number of columns.
    *
    * @return The width of this board.
    */
  def getWidth: Int = board.length

  /**
    * Returns the number of rows.
    *
    * @return The height of this board.
    */
  def getHeight: Int = board(0).length

  /**
    * Returns the square at the given <code>x,y</code> position.
    *
    * @param x
    * The <code>x</code> position (column) of the requested square.
    * @param y
    * The <code>y</code> position (row) of the requested square.
    * @return The square at the given <code>x,y</code> position (never null).
    */
  def squareAt(x: Int, y: Int): Square = {
    assert(withinBorders(x, y))
    val result = board(x)(y)
    assert(result != null, "Follows from invariant.")
    result
  }

  /**
    * Determines whether the given <code>x,y</code> position is on this board.
    *
    * @param x
    * The <code>x</code> position (row) to test.
    * @param y
    * The <code>y</code> position (column) to test.
    * @return <code>true</code> iff the position is on this board.
    */
  def withinBorders(x: Int, y: Int): Boolean = x >= 0 && x < getWidth && y >= 0 && y < getHeight
}