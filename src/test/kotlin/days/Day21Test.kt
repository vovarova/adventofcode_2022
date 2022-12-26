package days

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day21Test {
    @Test
    fun part1() {
        val day = Day21()
        Assertions.assertEquals(152.toBigDecimal(), day.partOne())
    }

    @Test
    fun part2() {
        val day = Day21()
        Assertions.assertEquals(301.toBigDecimal(), day.partTwo())
    }
    
}