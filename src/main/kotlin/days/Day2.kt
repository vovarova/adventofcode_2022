package days

class Day2 : Day(2) {

    object GameRules {
        const val winGamePoint = 6
        const val loseGamePoint = 0
        const val drawGamePoint = 3

        val gameElements = listOf(
            GameElement(name = "Rock", id = 'A', point = 1, winsFromId = 'C', looseFromId = 'B'),
            GameElement(name = "Paper", id = 'B', point = 2, winsFromId = 'A', looseFromId = 'C'),
            GameElement(name = "Scissors", id = 'C', point = 3, winsFromId = 'B', looseFromId = 'A')
        ).map { it.id to it }.toMap()

        class GameElement(
            val name: String,
            val id: Char,
            val point: Int,
            private val winsFromId: Char,
            private val looseFromId: Char
        ) {
            fun calcPoint(opponentMove: Char): Int {
                return if (opponentMove == id) {
                    point + drawGamePoint
                } else if (opponentMove == winsFromId) {
                    point + winGamePoint
                } else {
                    point + loseGamePoint
                }
            }

            fun winsFrom(): GameElement {
                return gameElements.get(winsFromId)!!
            }

            fun looseFrom(): GameElement {
                return gameElements.get(looseFromId)!!
            }
        }
    }

    class GameMove(val line: String) {
        fun yourMove(): Char {
            return line[2]
        }

        fun opponentMove(): Char {
            return line[0]
        }
    }

    override fun partOne(): Any {
        val elementConversion = mapOf(
            Pair('X', 'A'),
            Pair('Y', 'B'),
            Pair('Z', 'C'),
        )
        return inputList.map { GameMove(it) }.map {
            GameRules.gameElements.get(elementConversion[it.yourMove()]!!)!!.calcPoint(it.opponentMove())
        }.sum()
    }

    override fun partTwo(): Any {

        /**
         *         X means you need to lose
         *         Y means you need to end the round in a draw
         *       and Z means you need to win
         */
        return inputList.map { GameMove(it) }.map {
            if (it.yourMove() == 'X') {
                GameRules.gameElements.get(it.opponentMove())!!.winsFrom().calcPoint(it.opponentMove())
            } else if (it.yourMove() == 'Y') {
                GameRules.gameElements.get(it.opponentMove())!!.calcPoint(it.opponentMove())
            } else {
                GameRules.gameElements.get(it.opponentMove())!!.looseFrom().calcPoint(it.opponentMove())
            }
        }.sum()
    }
}
