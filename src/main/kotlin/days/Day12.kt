package days

import java.util.*


class Day12() : Day(12) {

    class HeightMap(input: List<String>) {
        val heightMap: Array<Array<Char>>
        val start: Cell
        val end: Cell
        val acells: List<Cell>

        init {
            val rows = input.size
            val columns = input[0].length
            val aCells = mutableListOf<Cell>()
            heightMap = Array(rows) { Array(columns) { 'a' } }
            var startIntermediate = Cell(0, 0)
            var endIntermediate = Cell(0, 0)
            for (i in 0 until rows) {
                for (j in 0 until columns) {
                    if (input[i][j] == 'S') {
                        startIntermediate = Cell(i, j)
                    } else if (input[i][j] == 'E') {
                        endIntermediate = Cell(i, j)
                    } else if (input[i][j] == 'a') {
                        aCells.add(Cell(i, j))
                    }
                    heightMap[i][j] = input[i][j]
                }
            }
            acells = aCells
            start = startIntermediate
            end = endIntermediate
        }

        fun startPath(): Path = Path(listOf(start))

        fun startPathWithAllA(): List<Path> {
            return (acells + start).map { Path(listOf(it)) }
        }


        inner class Cell(val row: Int, val column: Int) {
            fun up(): Cell = Cell(row + 1, column)
            fun down(): Cell = Cell(row - 1, column)
            fun left(): Cell = Cell(row, column - 1)
            fun right(): Cell = Cell(row, column + 1)
            fun equalsCell(other: Cell): Boolean {
                return this.row == other.row && this.column == other.column
            }

            fun isValidCell(): Boolean {
                return row >= 0 && row < heightMap.size
                        && column >= 0 && column < heightMap[row].size
            }

            fun toPair(): Pair<Int, Int> = Pair(row, column)

            fun getValue(): Char {
                return if (equalsCell(start)) {
                    'a'
                } else if (equalsCell(end)) {
                    'z'
                } else {
                    heightMap[row][column]
                }
            }
        }

        inner class Path(val cells: List<Cell>) {
            fun isFinished(): Boolean = cells.last().equalsCell(end)

            fun head(): Cell = cells.last()

            fun addCell(cell: Cell): Path = Path(cells + cell)

            fun possiblePath(): List<Path> {
                val last = cells.last()
                return listOf(last.up(), last.down(), last.left(), last.right())
                    .filter { it.isValidCell() }.filter {
                        it.getValue().code <= last.getValue().code + 1
                    }
                    .map { this.addCell(it) }
            }

        }
    }


    fun breadthForSearch(startPath: List<HeightMap.Path>): HeightMap.Path {
        val fifoQueue = LinkedList<HeightMap.Path>()
        fifoQueue.addAll(startPath)
        val visitedCells: MutableSet<Pair<Int, Int>> = mutableSetOf()
        while (fifoQueue.isNotEmpty()) {
            val poll = fifoQueue.poll()
            val possiblePath = poll.possiblePath()
                .filter { visitedCells.add(it.head().toPair()) }
            fifoQueue.addAll(possiblePath)
            val finished = possiblePath.find { it.isFinished() }
            if (finished != null) {
                return finished
            }
        }
        return startPath.last()
    }

    override fun partOne(): Any {
        return breadthForSearch(listOf(HeightMap(inputList).startPath())).cells.size-1
    }

    override fun partTwo(): Any {
        return breadthForSearch(HeightMap(inputList).startPathWithAllA()).cells.size-1
    }

}