package days

class Day10 : Day(10) {

    abstract class Command {
        abstract fun execute(value: Int): Int
    }

    class AddX(val commandValue: Int) : Command() {
        override fun execute(value: Int): Int {
            return commandValue + value
        }
    }

    object Noop : Command() {
        override fun execute(value: Int): Int = value
    }


    fun valueRegister(): Array<Int> {
        val commands = inputList.map {
            when (it) {
                "noop" -> listOf(Noop)
                else -> listOf(Noop, AddX(it.split(" ")[1].toInt()))
            }
        }.flatMap { it }
        return IntRange(1, commands.size).fold(Array(commands.size + 1) { i -> 1 }) { array, index ->
            array.also { array[index] = commands[index - 1].execute(array[index - 1]) }
        }
    }


    override fun partOne(): Any {
        return arrayOf(20, 60, 100, 140, 180, 220).map { valueRegister()[it - 1] * it }.sum()
    }


    override fun partTwo(): Any {
        val sprite = valueRegister()
        val screenCrt = listOf(
            IntRange(1, 40),
            IntRange(41, 80),
            IntRange(81, 120),
            IntRange(121, 160),
            IntRange(161, 200),
            IntRange(201, 240)
        ).map {
            it.mapIndexed { schreenPosition, striteIndex ->
                IntRange(
                    sprite[striteIndex - 1] - 1,
                    sprite[striteIndex - 1] + 1
                ).contains(schreenPosition)
            }.map {
                if (it) {
                    '#'
                } else {
                    '.'
                }
            }.joinToString(separator = "")
        }.joinToString(separator = System.lineSeparator())
        return screenCrt
    }
}