package games.game2048

import board.Cell
import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

/*
 * Your task is to implement the game 2048 https://en.wikipedia.org/wiki/2048_(video_game)
 * Implement the utility methods below.
 *
 * After implementing it you can try to play the game executing 'PlayGame2048'
 * (or choosing the corresponding run configuration).
 */
fun newGame2048(initializer: Game2048Initializer<Int> = RandomGame2048Initializer): Game =
        Game2048(initializer)

class Game2048(private val initializer: Game2048Initializer<Int>) : Game {
    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        repeat(2) {
            board.addNewValue(initializer)
        }
    }

    override fun canMove() = board.any { it == null }

    override fun hasWon() = board.any { it == 2048 }

    override fun processMove(direction: Direction) {
        if (board.moveValues(direction)) {
            board.addNewValue(initializer)
        }
    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }

}

/*
 * Add a new value produced by 'initializer' to a specified cell in a board.
 */
fun GameBoard<Int?>.addNewValue(initializer: Game2048Initializer<Int>) {
    val newValue = initializer.nextValue(this)
    if (newValue != null) this[newValue.first] = newValue.second
}

/*
 * Move values in a specified rowOrColumn only.
 * Use the helper function 'moveAndMergeEqual' (in Game2048Helper.kt).
 * The values should be moved to the beginning of the row (or column), in the same manner as in the function 'moveAndMergeEqual'.
 * Return 'true' if the values were moved and 'false' otherwise.
 */
fun GameBoard<Int?>.moveValuesInRowOrColumn(rowOrColumn: List<Cell>): Boolean {
    val rowOrColumnValues = rowOrColumn.map { this[it] }
    val movedAndMergedValues = rowOrColumnValues.moveAndMergeEqual { value: Int -> value + value }

    return rowOrColumn.foldIndexed(false) { index, moved, cell ->
        val previousValue = this[cell]
        this[cell] = movedAndMergedValues.getOrNull(index)
        moved || previousValue != movedAndMergedValues.getOrNull(index)
    }
}

/*
 * Move values by the rules of the 2048 game to the specified direction.
 * Use the 'moveValuesInRowOrColumn' function above.
 * Return 'true' if the values were moved and 'false' otherwise.
 */
fun GameBoard<Int?>.moveValues(direction: Direction): Boolean = when (direction) {
    Direction.RIGHT ->
        (1..width).fold(false) { moved, rowNumber ->
            moveValuesInRowOrColumn(this.getRow(rowNumber, width..1)) || moved
        }
    Direction.LEFT ->
        (1..this.width).fold(false) { moved, rowNumber ->
            moveValuesInRowOrColumn(this.getRow(rowNumber, 1..this.width)) || moved
        }
    Direction.UP ->
        (1..this.width).fold(false) { moved, columnNumber ->
            moveValuesInRowOrColumn(this.getColumn(1..this.width, columnNumber)) || moved
        }
    Direction.DOWN ->
        (1..this.width).fold(false) { moved, columnNumber ->
            moveValuesInRowOrColumn(this.getColumn(this.width..1, columnNumber)) || moved
        }
}