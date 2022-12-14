package days

import util.Cell


class Day14 : Day(14) {

    /*    498,4 -> 498,6 -> 496,6
        503,4 -> 502,4 -> 502,9 -> 494,9*/

    class RockMap(input: List<String>) {
        val rock: Set<Cell>
        var maxRow: Int
        val sand: MutableSet<Cell> = mutableSetOf()

        init {
            rock = createRockIndex(input)
            maxRow = rock.map { it.row }.max()
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

        fun fallSand(init: Cell): Boolean {
            var currentPosition = init
            while (currentPosition.row < maxRow) {
                val first = listOf(
                    currentPosition.up(),
                    currentPosition.up().left(),
                    currentPosition.up().right()
                ).find { !(rock.contains(it) || sand.contains(it)) }
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
            val floorRow = maxRow + 2
            while (currentPosition.row < floorRow) {
                val first = listOf(
                    currentPosition.up(),
                    currentPosition.up().left(),
                    currentPosition.up().right()
                ).find { !(rock.contains(it) || sand.contains(it) || it.row == floorRow) }
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
        while (rockMap.fallSand(Cell(0, 500))) {
        }
        return rockMap.sand.size
    }

    override fun partTwo(): Any {

        val rockMap = RockMap(inputList)
        while (rockMap.fallSandWithInfiniteFloor(Cell(0, 500))) {
        }
        return rockMap.sand.size
    }
}