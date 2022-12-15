package days

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import util.Cell

class Day15Test {

    @Test
    fun part1() {
        val day15 = Day15()
        Assertions.assertEquals(26, day15.partOne(10))
    }
    @Test
    fun part2() {
        val day15 = Day15()
        Assertions.assertEquals(56000011.toLong(), day15.partTwo(20))
    }

    @Test
    fun manhatanDistance() {
        Assertions.assertEquals(9, Util.manhattanDistance(Cell(7, 8), Cell(10, 2)))
    }
}