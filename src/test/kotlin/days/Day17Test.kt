package days

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day17Test {
    @Test
    fun part1() {
        val day = Day17()
        Assertions.assertEquals(3068, day.partOne())
    }

    @Test
    fun part2() {
        val day = Day17()
        Assertions.assertEquals(1514285714288L, day.partTwo())
    }
}