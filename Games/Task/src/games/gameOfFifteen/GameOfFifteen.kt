package games.gameOfFifteen

import board.*
import games.game.Game

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'
 * (or choosing the corresponding run configuration).
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
        GameOfFifteen(initializer)

class GameOfFifteen(private val initializer: GameOfFifteenInitializer) : Game {
    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        board.addNewValues(initializer)

    }

    override fun canMove() = true

    override fun hasWon() = board.getAllCells().map { board[it] ?: 0 } == (1..15) + 0

    override fun processMove(direction: Direction) {
        val cellWithEmptyNeighbour = board.findCellWithEmptyNeighbour(direction)
        if (cellWithEmptyNeighbour != null) board.swapCellWithNeighbour(cellWithEmptyNeighbour, direction)
    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }
}

fun GameBoard<Int?>.findCellWithEmptyNeighbour(direction: Direction): Cell? = this.getAllCells().find { cell ->
        val neighbour = cell.getNeighbour(direction)
        neighbour != null && this[neighbour] == null
    }

fun GameBoard<Int?>.swapCellWithNeighbour(cell: Cell, direction: Direction) {
    val neighbour = cell.getNeighbour(direction)
    if (neighbour != null) {
        this[neighbour] = this[cell]
        this[cell] = null
    }
}

fun GameBoard<Int?>.addNewValues(initializer: GameOfFifteenInitializer) {
    this.getAllCells().forEachIndexed { index, cell ->
        this[cell] = initializer.initialPermutation.getOrNull(index)
    }
}

