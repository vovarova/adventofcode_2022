package days

class Day10 : Day(10) {

    val commands = inputList.map {
        when (it) {
            "noop" -> Command.Noop
            else -> Command.AddX(it.split(" ")[1].toInt())
        }
    }

    class InstructionExecutor(commandsInput: List<Command>) {
        private val operations: List<Operation> = commandsInput.flatMap { it.operations }

        /**
         * valueRegistry[1] means value for end of 1-st cycle,and value for 2-nd cycle.
         */
        val valueRegistry: List<Int>

        init {
            valueRegistry =
                IntRange(1, operations.size).fold(mutableListOf(1)) { list, endCycle ->
                    list.also { list.add(endCycle, operations[endCycle - 1].execute(list[endCycle - 1])) }
                }
        }

        fun value(cycle: Int): Int {
            return valueRegistry[cycle - 1]
        }
    }

    open class Command(val operations: List<Operation>) {
        object Noop : Command(listOf(Operation.Wait))
        class AddX(value: Int) : Command(listOf(Operation.Wait, Operation.Change(value)))
    }

    abstract class Operation {
        abstract fun execute(value: Int): Int
        class Change(private val commandValue: Int) : Operation() {
            override fun execute(value: Int): Int {
                return commandValue + value
            }
        }

        object Wait : Operation() {
            override fun execute(value: Int): Int = value
        }
    }

    override fun partOne(): Any {
        val instructionExecutor = InstructionExecutor(commands)
        return arrayOf(20, 60, 100, 140, 180, 220).map { instructionExecutor.value(it) * it }.sum()
    }


    override fun partTwo(): Any {
        val sprite = InstructionExecutor(commands)
        val screenCrt = listOf(
            IntRange(1, 40),
            IntRange(41, 80),
            IntRange(81, 120),
            IntRange(121, 160),
            IntRange(161, 200),
            IntRange(201, 240)
        ).map {
            it.mapIndexed { screenPosition, striteIndex ->
                IntRange(
                    sprite.value(striteIndex) - 1,
                    sprite.value(striteIndex) + 1,
                ).contains(screenPosition)
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