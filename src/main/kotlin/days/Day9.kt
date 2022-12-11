package days

class Day9 : Day(9) {

    class Move(val command: Char, val times: Int)

    val moves: List<Move> = inputList.map { it.split(" ").let { Move(it[0][0], it[1].toInt()) } }

    data class KnotPosition(val row: Int = 0, val column: Int = 0) {
        fun up(): KnotPosition = KnotPosition(row + 1, column)
        fun down(): KnotPosition = KnotPosition(row - 1, column)
        fun left(): KnotPosition = KnotPosition(row, column - 1)
        fun right(): KnotPosition = KnotPosition(row, column + 1)

        fun diagonalSiblings(): List<KnotPosition> {
            return listOf(up().left(), up().right(), down().left(), down().right())
        }

        fun straightSiblings(): List<KnotPosition> {
            return listOf(up(), right(), down(), left())
        }

        fun singleStep(command: Char): KnotPosition {
            return when (command) {
                'U' -> up()
                'R' -> right()
                'L' -> left()
                'D' -> down()
                else -> up()
            }
        }

        fun isSibling(other: KnotPosition): Boolean {
            return (listOf(this) + straightSiblings() + diagonalSiblings()).any { it.equals(other) }
        }

        fun follow(other: KnotPosition): KnotPosition {
            if (!isSibling(other)) {
                if (row == other.row || column == other.column) {
                    return straightSiblings().first { it.isSibling(other) }
                }
                return diagonalSiblings().first { it.isSibling(other) }
            }
            return this
        }
    }


    override fun partOne(): Any {
        var headKnotPosition = KnotPosition()
        var tailKnotPosition = KnotPosition()
        val positions = mutableSetOf<KnotPosition>()
        positions.add(tailKnotPosition)
        for (move in moves) {
            for (iter in 1..move.times) {
                headKnotPosition = headKnotPosition.singleStep(move.command)
                tailKnotPosition = tailKnotPosition.follow(headKnotPosition)
                positions.add(tailKnotPosition)
            }
        }

        return positions.size
    }


    override fun partTwo(): Any {
        val knots = Array(10) {
            KnotPosition()
        }
        val positions = mutableSetOf<KnotPosition>()
        positions.add(knots.last())
        for (move in moves) {
            for (iter in 1..move.times) {
                knots[0] = knots[0].singleStep(move.command)
                for (knotIndex in 1 until knots.size) {
                    knots[knotIndex] = knots[knotIndex].follow(knots[knotIndex - 1])
                }
                positions.add(knots.last())
            }
        }
        return positions.size
    }
}