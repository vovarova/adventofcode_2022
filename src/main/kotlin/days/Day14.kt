package days

import util.Cell
import util.range


class Day14 : Day(14) {

    class CaveMap(input: List<String>) {
        val rock: Set<Cell>
        var maxRow: Int
        val sand: MutableSet<Cell> = mutableSetOf()

        init {
            rock = createRockIndex(input)
            maxRow = rock.map { it.row }.max()
        }

        fun createRockIndex(input: List<String>): Set<Cell> {
            return input.flatMap { line ->
                line.split(" -> ").map { it.split(",").let { Cell(row = it[1].toInt(), column = it[0].toInt()) } }
                    .windowed(2) { Pair(it[0], it[1]) }.flatMap { it ->
                        val columnRange = range(it.first.column, it.second.column)
                        val rowRange = range(it.first.row, it.second.row)
                        rowRange.flatMap { row -> columnRange.map { column -> Cell(row, column) } }
                    }
            }.toSet()
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
        val caveMap = CaveMap(inputList)
        while (caveMap.fallSand(Cell(0, 500))) {
        }
        return caveMap.sand.size
    }

    override fun partTwo(): Any {

        val caveMap = CaveMap(inputList)
        while (caveMap.fallSandWithInfiniteFloor(Cell(0, 500))) {
        }
        return caveMap.sand.size
    }
}