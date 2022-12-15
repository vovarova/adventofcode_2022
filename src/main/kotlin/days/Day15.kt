package days

import days.Util.manhattanDistance
import util.Cell
import kotlin.math.abs


class Day15 : Day(15) {

    class Sensor(val coordinates: Cell, val beacon: Cell) {
        val beaconDistance: Int = manhattanDistance(coordinates, beacon)
        fun distanceTo(cell: Cell): Int = manhattanDistance(coordinates, cell)

        fun covers(cell: Cell): Boolean {
            return beaconDistance >= distanceTo(cell)
        }

        fun maxColumnCovered(row: Int): Cell {
            return Cell(row, coordinates.column + abs(beaconDistance - abs(row - coordinates.row)))
        }

        fun minColumnCovered(row: Int): Cell {
            return Cell(row, coordinates.column - abs(beaconDistance - abs(row - coordinates.row)))
        }
    }

    class Sensors(input: List<String>) {
        val beacons: Set<Cell>
        val sensorMap: Map<Cell, Sensor>

        fun sensorsOnRow(row: Int, minColumn: Int, maxColumn: Int): List<Sensor> {
            return sensorMap.values.filter { it.coordinates.row == row && it.coordinates.column >= minColumn && it.coordinates.column <= maxColumn }
        }

        fun beaconsOnRow(row: Int, minColumn: Int, maxColumn: Int): List<Cell> {
            return beacons.filter { it.row == row && it.column >= minColumn && it.column <= maxColumn }
        }

        init {
            val regex = Regex("-?\\d+")
            sensorMap = input.map {
                regex.findAll(it).map { it.value.toInt() }.toList()
            }
                .map {
                    Sensor(Cell(it[1], it[0]), Cell(it[3], it[2]))
                }.map { it.coordinates to it }.toMap()
            beacons = sensorMap.values.map { it.beacon }.toSet()
        }

        fun coveredBySensor(cell: Cell): Sensor? {
            return sensorMap.values.asSequence().find { it.covers(cell) }
        }


    }

    fun partOne(row: Int): Int {
        val sensors = Sensors(inputList)
        val minColumn = sensors.sensorMap.values.map { it.minColumnCovered(row) }.map { it.column }.min()
        val maxColumn = sensors.sensorMap.values.map { it.maxColumnCovered(row) }.map { it.column }.max()
        var currentCell = Cell(row, minColumn)
        var count: Int = 0
        while (currentCell.column <= maxColumn) {
            val coveredBySensor = sensors.coveredBySensor(currentCell)
            if (coveredBySensor != null) {
                val maxColumnCovered = coveredBySensor.maxColumnCovered(currentCell.row)
                count += maxColumnCovered.column - currentCell.column + 1
                count -= sensors.sensorsOnRow(currentCell.row, currentCell.column, maxColumnCovered.column).size
                count -= sensors.beaconsOnRow(currentCell.row, currentCell.column, maxColumnCovered.column).size
                currentCell = maxColumnCovered
            }
            currentCell = currentCell.right()
        }
        return count
    }

    override fun partOne(): Int {
        return partOne(row = 2000000)
    }

    fun notCoveredCell(maxCoordinate: Int): Cell? {
        val sensors = Sensors(inputList)
        for (row in 0..maxCoordinate) {
            var currentCell = Cell(row, 0)
            while (currentCell.column <= maxCoordinate) {
                val coveredBySensor = sensors.coveredBySensor(currentCell)
                if (coveredBySensor != null) {
                    currentCell = coveredBySensor.maxColumnCovered(currentCell.row).right()
                } else {
                    return currentCell
                }
            }
        }
        return null
    }


    fun partTwo(maxCoordinate: Int): Any {
        val notCoveredCell = notCoveredCell(maxCoordinate)!!
        return notCoveredCell.column.toLong() * 4000000 + notCoveredCell.row.toLong()
    }


    override fun partTwo(): Any {
        return partTwo(4000000)
    }

}

object Util {
    fun manhattanDistance(c1: Cell, c2: Cell): Int =
        abs(c1.row - c2.row) + abs(c1.column - c2.column)
}

fun main() {
    Day15().partTwo()
}

