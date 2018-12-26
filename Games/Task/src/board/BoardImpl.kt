package board

import board.Direction.*
import java.lang.IllegalArgumentException
import kotlin.math.min

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)

private open class SquareBoardImpl(final override val width: Int) : SquareBoard {

    val cells: List<Cell>

    init {
        val columnIndexes = listOf(1..width).flatten()
        val rowIndexes = listOf(1..width).flatten()
        cells = columnIndexes.fold(listOf()) { allCells, columnIndex ->
            allCells + rowIndexes.map { rowIndex -> Cell(columnIndex, rowIndex) }
        }
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? = if (i in 1..width && j in 1..width) cells.first { it.i == i && it.j == j } else null

    override fun getCell(i: Int, j: Int): Cell = getCellOrNull(i, j) ?: throw IllegalArgumentException()

    override fun getAllCells(): Collection<Cell> = cells

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        return when {
            jRange.first > jRange.last -> {
                val rowRangeInBoard = jRange.last..(min(width, jRange.first))
                rowRangeInBoard.map { getCell(i, it) }.reversed()
            }
            else -> {
                val rowRangeInBoard = jRange.first..(min(width, jRange.last))
                rowRangeInBoard.map { getCell(i, it) }
            }
        }
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        return when {
            iRange.first > iRange.last -> {
                val columnRangeInBoard = iRange.last..(min(width, iRange.first))
                columnRangeInBoard.map { getCell(it, j) }.reversed()
            }
            else -> {
                val columnRangeInBoard = iRange.first..(min(width, iRange.last))
                columnRangeInBoard.map { getCell(it, j) }
            }
        }
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        val rowIndex = when (direction) {
            UP -> if (this.i - 1 in 1..this@SquareBoardImpl.width) this.i - 1 else null
            DOWN -> if (this.i + 1 in 1..this@SquareBoardImpl.width) this.i + 1 else null
            else -> this.i
        }
        val columnIndex = when (direction) {
            RIGHT -> if (this.j + 1 in 1..this@SquareBoardImpl.width) this.j + 1 else null
            LEFT -> if (this.j - 1 in 1..this@SquareBoardImpl.width) this.j - 1 else null
            else -> this.j
        }
        return if (rowIndex !== null && columnIndex !== null) this@SquareBoardImpl.cells.first { it.i == rowIndex && it.j == columnIndex } else null
    }
}

private class GameBoardImpl<T>(width: Int) : GameBoard<T>, SquareBoardImpl(width) {

    val cellValues: MutableMap<Cell, T?> = mutableMapOf()

    init {
        cells.forEach { cellValues[it] = null }
    }

    override fun get(cell: Cell): T? = cellValues[cell]

    override fun set(cell: Cell, value: T?) {
        cellValues[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> = cellValues.filter { predicate.invoke(it.value) }.keys

    override fun find(predicate: (T?) -> Boolean): Cell? = filter(predicate).firstOrNull()

    override fun any(predicate: (T?) -> Boolean): Boolean = cellValues.values.any(predicate)

    override fun all(predicate: (T?) -> Boolean): Boolean = cellValues.values.all(predicate)

}
