package days

import java.util.*

class Day5 : Day(5) {

    fun getMoves(inputList: List<String>): List<Move> {
        return inputList.drop(10)
            .map {
                "\\d+".toRegex().findAll(it).map { it.value }.toList().let {
                    Move(it[0].toInt(), it[1].toInt(), it[2].toInt())
                }
            }
    }

    fun getStacks(inputList: List<String>): List<LinkedList<Char>> {
        val stacks = IntRange(0, 9).map { LinkedList<Char>() }
        inputList.take(8)
            .forEach {
                it.chunked(4).forEachIndexed { index, element ->
                    run {
                        if (element.isNotEmpty() && element[0] == '[') {
                            stacks[index + 1].add(element[1])
                        }
                    }
                }
            }
        return stacks
    }

    class Move(val items: Int, val from: Int, val to: Int)


    override fun partOne(): Any {
        val stacks = getStacks(inputList)
        val moves = getMoves(inputList)
        moves.forEach {
            val from = stacks[it.from]
            val to = stacks[it.to]
            IntRange(1, it.items).forEach {
                to.addFirst(from.poll())
            }
        }
        return stacks.drop(1).map { it.first }.joinToString(separator = "")
    }

    override fun partTwo(): Any {
        val stacks = getStacks(inputList)
        val moves = getMoves(inputList)
        moves.forEach {
            val from = stacks[it.from]
            val to = stacks[it.to]
            IntRange(1, it.items).map { from.poll() }.reversed().forEach {
                to.addFirst(it)
            }
        }
        return stacks.drop(1).map { it.first }.joinToString(separator = "")
    }
}