package days

class Day4 : Day(4) {

    fun IntRange.contains(otherRange: IntRange): Boolean {
        return this.contains(otherRange.start) && this.contains(otherRange.endInclusive)
    }

    fun IntRange.overlaps(otherRange: IntRange): Boolean {
        return this.contains(otherRange.start) || this.contains(otherRange.endInclusive)
                || otherRange.contains(this.start) || otherRange.contains(this.endInclusive)
    }

    fun inputPairRange(): List<Pair<IntRange, IntRange>> {
        return inputList.map {
            val pairSplit = it.split(",")
            val firstPairRange = pairSplit[0].split("-").let {
                IntRange(it[0].toInt(), it[1].toInt())
            }
            val secondPaitRange = pairSplit[1].split("-").let {
                IntRange(it[0].toInt(), it[1].toInt())
            }
            Pair(firstPairRange, secondPaitRange)
        }
    }

    override fun partOne(): Any {
        return inputPairRange().count {
            it.first.contains(it.second) || it.second.contains(it.first)
        }
    }

    override fun partTwo(): Any {
        return inputPairRange().count {
            it.first.overlaps(it.second)
        }
    }
}
