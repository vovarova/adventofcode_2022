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
                return when (opponentMove) {
                    id -> point + drawGamePoint
                    winsFromId -> point + winGamePoint
                    else -> point + loseGamePoint
                }
            }

            fun winsFrom(): GameElement {
                return gameElements[winsFromId]!!
            }

            fun looseFrom(): GameElement {
                return gameElements[looseFromId]!!
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
        return inputList.map { GameMove(it) }.map {
            when (it.yourMove()) {
                'X' -> GameRules.gameElements['A']!!.calcPoint(it.opponentMove())
                'Y' -> GameRules.gameElements['B']!!.calcPoint(it.opponentMove())
                else -> GameRules.gameElements['C']!!.calcPoint(it.opponentMove())
            }
        }.sum()
    }

    override fun partTwo(): Any {

        /**
         *         X means you need to lose
         *         Y means you need to end the round in a draw
         *       and Z means you need to win
         */
        return inputList.map { GameMove(it) }.map {
            when (it.yourMove()) {
                'X' -> GameRules.gameElements[it.opponentMove()]!!.winsFrom().calcPoint(it.opponentMove())
                'Y' -> GameRules.gameElements[it.opponentMove()]!!.calcPoint(it.opponentMove())
                else -> GameRules.gameElements[it.opponentMove()]!!.looseFrom().calcPoint(it.opponentMove())
            }
        }.sum()
    }
}
