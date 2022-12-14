package days

import util.InputReader

abstract class Day(var inputString: String) {
    constructor(dayNumber: Int) : this(InputReader.getInputAsString(dayNumber))
    var inputList = inputString.lines()
    abstract fun partOne(): Any
    abstract fun partTwo(): Any
}
