package days

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class Day14Test {
    val testInput = """498,4 -> 498,6 -> 496,6
503,4 -> 502,4 -> 502,9 -> 494,9"""

    @Test
    fun listCheck() {
        val day14 = Day14()
        day14.inputList = testInput.lines()
        Assertions.assertEquals(24, day14.partOne())
    }


}