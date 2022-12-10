package days

import util.InputReader

abstract class Day(val inputString: String) {
    constructor(dayNumber: Int) : this(InputReader.getInputAsString(dayNumber))
    val inputList = inputString.lines()
    abstract fun partOne(): Any
    abstract fun partTwo(): Any
}
