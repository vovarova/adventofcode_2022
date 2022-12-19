package days

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day18Test {
    @Test
    fun part1() {
        val day = Day18()
        Assertions.assertEquals(64, day.partOne())
    }

    @Test
    fun part2() {
        val day = Day18()
        Assertions.assertEquals(1514285714288L, day.partTwo())
    }
}