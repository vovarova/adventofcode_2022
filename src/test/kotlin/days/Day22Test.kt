package days

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day22Test {
    @Test
    fun part1() {
        val day = Day22()
        Assertions.assertEquals(6032, day.partOne())
    }

    @Test
    fun part2() {
        val day = Day22()
        Assertions.assertEquals(301.toBigDecimal(), day.partTwo())
    }
    
}