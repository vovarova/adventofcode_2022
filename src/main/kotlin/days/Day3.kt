package days

class Day3 : Day(3) {


    private fun priority(c: Char): Int {
        return if (c.isLowerCase()) {
            c.code - 'a'.code + 1
        } else {
            c.code - 'A'.code + 27
        }
    }

    override fun partOne(): Any {
        return inputList.map {
            it.toCharArray(0, it.length / 2).toSet().intersect(it.toCharArray(it.length / 2).toSet()).iterator().next()
        }.map { priority(it) }.sum()
    }

    override fun partTwo(): Any {
        return inputList.chunked(3) {
            it[0].toSet().intersect(it[1].toSet()).intersect(it[2].toSet()).first()
        }.map { priority(it) }.sum()
    }
}
