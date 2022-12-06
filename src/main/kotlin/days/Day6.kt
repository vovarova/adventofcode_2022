package days

class Day6 : Day(6) {

    class UniqueCharCalculator() {
        private val index = mutableMapOf<Char, Int>()
        var uniqueChars = 0
        fun addChar(char: Char) {
            if (index[char] == 1) {
                uniqueChars--
            } else if (index[char] == null || index[char] == 0) {
                uniqueChars++
            }
            index.compute(char) { key, value -> value?.inc() ?: 1 }
        }

        fun removeChar(char: Char) {
            if (index[char] == 1) {
                uniqueChars--
            } else if (index[char] == 2) {
                uniqueChars++
            }
            index.compute(char) { _, value -> value?.dec() }
        }
    }

    fun indexOfUniqueNumbersSequence(inputString: String, expectedUniqueNumbers: Int): Int {
        val uniqueCharCalculator = UniqueCharCalculator()
        IntRange(0, expectedUniqueNumbers - 1).forEach {
            uniqueCharCalculator.addChar(inputString[it])
        }
        if (uniqueCharCalculator.uniqueChars == expectedUniqueNumbers) {
            return expectedUniqueNumbers
        }
        return IntRange(expectedUniqueNumbers, inputString.length - 1).map {
            uniqueCharCalculator.addChar(inputString[it])
            uniqueCharCalculator.removeChar(inputString[it - expectedUniqueNumbers])
            it + 1 to uniqueCharCalculator.uniqueChars
        }.first { it.second == expectedUniqueNumbers }.first
    }

    override fun partOne(): Any {
        return indexOfUniqueNumbersSequence(inputString,4)
    }

    override fun partTwo(): Any {
        return indexOfUniqueNumbersSequence(inputString,14)
    }
}