package days

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import util.Cell

class Day15Test {

    val input = """
Sensor at x=2, y=18: closest beacon is at x=-2, y=15
Sensor at x=9, y=16: closest beacon is at x=10, y=16
Sensor at x=13, y=2: closest beacon is at x=15, y=3
Sensor at x=12, y=14: closest beacon is at x=10, y=16
Sensor at x=10, y=20: closest beacon is at x=10, y=16
Sensor at x=14, y=17: closest beacon is at x=10, y=16
Sensor at x=8, y=7: closest beacon is at x=2, y=10
Sensor at x=2, y=0: closest beacon is at x=2, y=10
Sensor at x=0, y=11: closest beacon is at x=2, y=10
Sensor at x=20, y=14: closest beacon is at x=25, y=17
Sensor at x=17, y=20: closest beacon is at x=21, y=22
Sensor at x=16, y=7: closest beacon is at x=15, y=3
Sensor at x=14, y=3: closest beacon is at x=15, y=3
Sensor at x=20, y=1: closest beacon is at x=15, y=3
    """.trim()

    @Test
    fun part1() {
        val day15 = Day15()
        day15.inputList = input.lines()
        Assertions.assertEquals(26, day15.partOne(10))
    }
    @Test
    fun part2() {
        val day15 = Day15()
        day15.inputList = input.lines()
        Assertions.assertEquals(56000011.toLong(), day15.partTwo(20))
    }


    @Test
    fun manhatanDistance() {
        Assertions.assertEquals(9, Util.manhattanDistance(Cell(7, 8), Cell(10, 2)))
    }


}