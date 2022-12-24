package days

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day20Test {
    @Test
    fun part1() {
        val day = Day20()
        Assertions.assertEquals(3, day.partOne())
    }

    @Test
    fun part2() {
        val day = Day20()
        Assertions.assertEquals(1623178306L, day.partTwo())
    }
    
}