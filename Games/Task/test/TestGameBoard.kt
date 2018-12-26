import board.Cell
import board.GameBoard
import board.createGameBoard
import org.junit.Assert
import org.junit.Test

class TestGameBoard {
    operator fun <T> GameBoard<T>.get(i: Int, j: Int) = get(getCell(i, j))
    operator fun <T> GameBoard<T>.set(i: Int, j: Int, value: T) = set(getCell(i, j), value)


    private fun Cell?.asString() = if (this != null) "($i, $j)" else ""


    @Test
    fun testGetAndSetElement() {
        val gameBoard = createGameBoard<Char>(2)
        gameBoard[1, 1] = 'a'
        Assert.assertEquals('a', gameBoard[1, 1])
    }

    @Test
    fun testFilter() {
        val gameBoard = createGameBoard<Char>(2)
        gameBoard[1, 1] = 'a'
        gameBoard[1, 2] = 'b'
        val cells = gameBoard.filter { it == 'a' }
        Assert.assertEquals(1, cells.size)
        val cell = cells.first()
        Assert.assertEquals(1, cell.i)
        Assert.assertEquals(1, cell.j)
    }

    @Test
    fun testAll() {
        val gameBoard = createGameBoard<Char>(2)
        gameBoard[1, 1] = 'a'
        gameBoard[1, 2] = 'a'
        Assert.assertFalse(gameBoard.all { it == 'a' })
        gameBoard[2, 1] = 'a'
        gameBoard[2, 2] = 'a'
        Assert.assertTrue(gameBoard.all { it == 'a' })
    }

    @Test
    fun testAny() {
        val gameBoard = createGameBoard<Char>(2)
        gameBoard[1, 1] = 'a'
        gameBoard[1, 2] = 'b'
        Assert.assertTrue(gameBoard.any { it in 'a'..'b' })
        Assert.assertTrue(gameBoard.any { it == null })
    }


    @Test
    fun testFind() {
        val gameBoard = createGameBoard<Char>(2)
        gameBoard[1, 1] = 'a'
        gameBoard[1, 2] = 'a'
        val cell = gameBoard.find { it == 'a' }
        Assert.assertTrue(cell?.i == 1)
        Assert.assertTrue(cell?.j == 1)
        Assert.assertTrue(gameBoard.find { it == 'b' } == null)
    }


    @Test
    fun testDirections() {
        val gameBoard = createGameBoard<Char>(4)
        with(gameBoard) {
            val cell = getCellOrNull(1, 1)
            org.junit.Assert.assertNotNull(cell)
            org.junit.Assert.assertEquals(null, cell!!.getNeighbour(board.Direction.UP))
            org.junit.Assert.assertEquals(null, cell.getNeighbour(board.Direction.LEFT))
            org.junit.Assert.assertEquals("(2, 1)", cell.getNeighbour(board.Direction.DOWN).asString())
            org.junit.Assert.assertEquals("(1, 2)", cell.getNeighbour(board.Direction.RIGHT).asString())
        }
        //Wrong neighbour for the cell (2, 3) in a direction UP expected:<(1, [3])> but was:<(1, [2])>
        with(gameBoard) {
            val cell = getCellOrNull(2, 3)
            org.junit.Assert.assertNotNull(cell)
            org.junit.Assert.assertEquals("(1, 3)", cell?.getNeighbour(board.Direction.UP).asString())
            org.junit.Assert.assertEquals("(3, 3)", cell?.getNeighbour(board.Direction.DOWN).asString())
            org.junit.Assert.assertEquals("(2, 2)", cell?.getNeighbour(board.Direction.LEFT).asString())
            org.junit.Assert.assertEquals("(2, 4)", cell?.getNeighbour(board.Direction.RIGHT).asString())
        }
    }

}