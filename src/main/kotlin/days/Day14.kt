package days


class Day14 : Day(14) {

    /*    498,4 -> 498,6 -> 496,6
        503,4 -> 502,4 -> 502,9 -> 494,9*/

    class RockMap(input: List<String>, increaseMax: Int = 0) {
        val rock: Set<Cell>
        var maxRow: Int
        val sand: MutableSet<Cell> = mutableSetOf()

        init {
            rock = createRockIndex(input)
            maxRow = rock.map { it.row }.max() + increaseMax
        }

        fun createRockIndex(input: List<String>): Set<Cell> {
            val rockIndex: MutableSet<Cell> = mutableSetOf()
            for (line in input) {
                val split = line.split(" -> ")
                    .map { it.split(",").let { Pair(it[1].toInt(), it[0].toInt()) } }
                for (i in 1 until split.size) {
                    val start = split[i - 1]
                    val end = split[i]
                    // same row
                    var rowRange: IntProgression
                    var columnRange: IntProgression
                    if (start.first == end.first) {
                        columnRange = IntProgression.fromClosedRange(
                            start.second,
                            end.second,
                            Integer.signum(end.second - start.second)
                        )
                        rowRange = IntRange(start.first, start.first)
                    } else {
                        rowRange = IntProgression.fromClosedRange(
                            start.first,
                            end.first,
                            Integer.signum(end.first - start.first)
                        )
                        columnRange = IntRange(start.second, start.second)
                    }
                    for (row in rowRange) {
                        for (column in columnRange) {
                            rockIndex.add(Cell(row, column))
                        }
                    }
                }
            }
            return rockIndex
        }


        fun cell(row: Int, column: Int): Cell = Cell(row, column)

        inner class Cell(val row: Int, val column: Int) {
            val pair = Pair(row, column)
            fun up(): Cell = Cell(row + 1, column)
            fun down(): Cell = Cell(row - 1, column)
            fun left(): Cell = Cell(row, column - 1)
            fun right(): Cell = Cell(row, column + 1)
            fun toPair() = pair

            fun isValid(): Boolean {
                return !(rock.contains(this) || sand.contains(this))
            }

            fun isValidWithInfiniteFloor(): Boolean {
                return !(rock.contains(this) || sand.contains(this) || row == maxRow)
            }

            override fun equals(other: Any?): Boolean {
                if (other is Cell) {
                    return this.pair.equals(other.pair)
                }
                return false
            }

            override fun hashCode(): Int {
                return pair.hashCode()
            }
        }

        fun fallSand(init: Cell): Boolean {
            var currentPosition = init
            while (currentPosition.row < maxRow) {
                val first = listOf(
                    currentPosition.up(),
                    currentPosition.up().left(),
                    currentPosition.up().right()
                ).find { it.isValid() }
                if (first == null) {
                    sand.add(currentPosition)
                    return true
                } else
                    currentPosition = first
            }
            return false
        }

        fun fallSandWithInfiniteFloor(init: Cell): Boolean {
            if (sand.contains(init)) {
                return false
            }
            var currentPosition = init
            while (currentPosition.row < maxRow) {
                val first = listOf(
                    currentPosition.up(),
                    currentPosition.up().left(),
                    currentPosition.up().right()
                ).find { it.isValidWithInfiniteFloor() }
                if (first == null) {
                    sand.add(currentPosition)
                    return true
                } else
                    currentPosition = first
            }
            return currentPosition != init
        }
    }


    override fun partOne(): Int {
        val rockMap = RockMap(inputList)
        while (rockMap.fallSand(rockMap.cell(0, 500))) {
        }
        return rockMap.sand.size
    }

    override fun partTwo(): Any {
        val rockMap = RockMap(inputList, 2)
        while (rockMap.fallSandWithInfiniteFloor(rockMap.cell(0, 500))) {
        }
        return rockMap.sand.size
    }
}