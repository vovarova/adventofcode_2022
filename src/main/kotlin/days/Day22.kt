package days

import util.Cell


class Day22 : Day(22) {


    class Move(val points: Int, val direction: Char)

    class Board(val inputs: List<String>) {
        val valueMap: Map<Cell, Char>
        val moves: List<Move>
        val leftBoarderRows: Array<Cell>
        val rightBoarderRows: Array<Cell>
        val topBoarderColumns: Array<Cell>
        val bottomBoarderColumns: Array<Cell>

        init {
            val rows = inputs.indexOf("")
            val columns = inputs.map { it.length }.take(rows).max()
            val currentValueMap = mutableMapOf<Cell, Char>()
            valueMap = currentValueMap
            inputs.take(rows).forEachIndexed { row, line ->
                line.forEachIndexed { column, char ->
                    if (char != ' ') {
                        currentValueMap[Cell(row + 1, column + 1)] = char
                    }
                }
            }

            leftBoarderRows = Array(rows + 1) { row ->
                currentValueMap.keys.filter { row == it.row }.minByOrNull { it.column } ?: Cell(0, 0)
            }
            rightBoarderRows = Array(rows + 1) { row ->
                currentValueMap.keys.filter { row == it.row }.maxByOrNull { it.column } ?: Cell(0, 0)
            }
            topBoarderColumns = Array(columns + 1) { column ->
                currentValueMap.keys.filter { column == it.column }.minByOrNull { it.row } ?: Cell(0, 0)
            }
            bottomBoarderColumns = Array(columns + 1) { column ->
                currentValueMap.keys.filter { column == it.column }.maxByOrNull { it.row } ?: Cell(0, 0)
            }

            val split = inputs[rows + 1].replace("R", " R ").replace("L", " L ").split(" ")
            moves = listOf(Move(points = split[0].toInt(), direction = 'R')) + split.drop(1)
                .windowed(size = 2, step = 2).map {
                    Move(points = it[1].toInt(), direction = it[0].get(0))
                }
        }

        fun move(cell: Cell, nextCell: Cell, boarderCell: Array<Cell>, func: (Cell) -> Int): Cell {
            if (valueMap[nextCell] == '#') {
                return cell
            }
            if (valueMap[nextCell] == null) {
                val cellFromAnotherSide = boarderCell[func.invoke(nextCell)]
                if (valueMap[cellFromAnotherSide] == '#') {
                    return cell
                } else {
                    return cellFromAnotherSide
                }
            } else {
                return nextCell
            }
        }


        fun left(cell: Cell): Cell {
            return move(cell, cell.left(), rightBoarderRows) {
                it.row
            }
        }

        fun right(cell: Cell): Cell {
            return move(cell, cell.right(), leftBoarderRows) {
                it.row
            }
        }

        fun up(cell: Cell): Cell {
            return move(cell, cell.up(), bottomBoarderColumns) {
                it.column
            }
        }

        fun down(cell: Cell): Cell {
            return move(cell, cell.down(), topBoarderColumns) {
                it.column
            }
        }
    }


    override fun partOne(): Any {
        val board = Board(inputList)
        var currentCell = board.leftBoarderRows[1].left()

        val moveMap = mapOf<Pair<Char, Char>, Pair<Char, (Cell) -> Cell>>(
            Pair('R', 'R') to Pair('D', board::down),
            Pair('R', 'L') to Pair('U', board::up),
            Pair('L', 'R') to Pair('U', board::up),
            Pair('L', 'L') to Pair('D', board::down),
            Pair('U', 'L') to Pair('L', board::left),
            Pair('U', 'R') to Pair('R', board::right),
            Pair('D', 'L') to Pair('R', board::right),
            Pair('D', 'R') to Pair('L', board::left),
        )
        var previousMove = 'U'
        for (move in board.moves) {
            val nextDirection = moveMap[previousMove to move.direction]!!
            previousMove = nextDirection.first
            for (iter in 1..move.points) {
                val nextCell = nextDirection.second.invoke(currentCell)
                println("$nextCell direction ${nextDirection.first}")
                if (currentCell == nextCell) {
                    break
                }
                currentCell = nextCell
            }
        }
        val moveScore = mapOf('R' to 0, 'D' to 1, 'L' to 2, 'U' to 4)
        return currentCell.row * 1000 + 4 * currentCell.column + moveScore[previousMove]!!
    }

    override fun partTwo(): Any {
        return 0
    }
}