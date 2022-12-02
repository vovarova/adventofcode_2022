package days

class Day2 : Day(2) {
    val gameRules = mapOf(
        Pair('A', Pair('C', 'B')),
        Pair('B', Pair('A', 'C')),
        Pair('C', Pair('B', 'A')),
    )

    val elementPoints = mapOf(
        Pair('A', 1),
        Pair('B', 2),
        Pair('C', 3),
    )

    fun calcPoints(opponentMove: Char, yourMove: Char): Int {
        var points = 0
        points += elementPoints[yourMove]!!
        if (opponentMove == yourMove) {
            points += 3
        } else if (opponentMove == gameRules[yourMove]!!.first) {
            points += 6
        }
        return points
    }

    override fun partOne(): Any {
        val elementConversion = mapOf(
            Pair('X', 'A'),
            Pair('Y', 'B'),
            Pair('Z', 'C'),
        )
        return inputList.map {
            calcPoints(it[0], elementConversion[it[2]]!!)
        }.sum()
    }

    override fun partTwo(): Any {
        return inputList.map {
            val yourMove = if (it[2] == 'X') {
                gameRules[it[0]]!!.first
            } else if (it[2] == 'Y') {
                it[0]
            } else {
                gameRules[it[0]]!!.second
            }
            calcPoints(it[0], yourMove)
        }.sum()
    }
}
