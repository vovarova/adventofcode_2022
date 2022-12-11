package days

import kotlin.properties.Delegates


class Day11() : Day(11) {

    class MonkeyCollection(value: List<String>) {
        val monkeys = extractMonkeys(value)

        fun String.startWithInteger(startsWith: String, endWith: String = ""): Int? {
            if (this.startsWith(startsWith)) {
                return this.replaceFirst(startsWith, "").replace(endWith, "").toInt()
            }
            return null
        }

        fun String.startWithIntegers(startsWith: String, delimiter: String): List<Int>? {
            if (this.startsWith(startsWith)) {
                return this.replaceFirst(startsWith, "").split(delimiter).map { it.toInt() }.toList()
            }
            return null
        }

        fun extractMonkeys(value: List<String>): List<Monkey> {
            val monkeys = mutableListOf<Monkey>()
            var currentMonkey: Monkey? = null
            for (line in value) {
                line.startWithInteger("Monkey ", ":")?.also { currentMonkey = Monkey(it).also { monkeys.add(it) } }
                line.startWithIntegers("  Starting items: ", ", ")?.also {
                    it.also { currentMonkey?.startingItems = it }
                }
                if (line.startsWith("  Operation: new = old * old")) {
                    currentMonkey?.operation = Monkey.MonkeyOperation.Square()
                } else {
                    line.startWithInteger("  Operation: new = old * ")?.also {
                        currentMonkey?.operation = Monkey.MonkeyOperation.Multiply(it)
                    }
                }
                line.startWithInteger("  Operation: new = old + ")?.also {
                    currentMonkey?.operation = Monkey.MonkeyOperation.Sum(it)
                }
                line.startWithInteger("  Test: divisible by ")?.also {
                    currentMonkey?.testDivision = it
                }
                line.startWithInteger("  Test: divisible by ")?.also {
                    currentMonkey?.testDivision = it
                }

                line.startWithInteger("    If true: throw to monkey ")?.also {
                    currentMonkey?.ifTrueMonkey = it
                }
                line.startWithInteger("    If false: throw to monkey ")?.also {
                    currentMonkey?.ifFalseMonkey = it
                }
            }

            return monkeys

        }


        class Monkey(val monkeyIndex: Int) {
            var startingItems: List<Int> by Delegates.notNull()
            var operation: MonkeyOperation by Delegates.notNull()
            var testDivision: Int by Delegates.notNull()
            var ifTrueMonkey: Int by Delegates.notNull()
            var ifFalseMonkey: Int by Delegates.notNull()

            sealed interface MonkeyOperation {
                class Multiply(val number: Int) : MonkeyOperation
                class Square() : MonkeyOperation
                class Sum(val number: Int) : MonkeyOperation
            }
        }
    }


    interface Number<T> {
        operator fun times(number: Int): T
        operator fun plus(number: Int): T
        fun divides(number: Int): Boolean
        fun sqr(): T
        fun div(number: Int): T
        class SimpleNumber(private val initialVal: Int) : Number<SimpleNumber> {
            override fun times(number: Int): SimpleNumber = SimpleNumber(number * initialVal)
            override fun plus(number: Int): SimpleNumber = SimpleNumber(number + initialVal)
            override fun divides(number: Int): Boolean = initialVal % number == 0
            override fun sqr(): SimpleNumber = SimpleNumber(initialVal * initialVal)
            override fun div(number: Int): SimpleNumber = SimpleNumber(initialVal / number)
        }


        class NumberReminder(val divisionBase: Int, initialValue: Int) {
            val remainder = initialValue % divisionBase
            operator fun plus(number: Int): NumberReminder {
                return NumberReminder(divisionBase, remainder + number)
            }

            operator fun times(number: Int): NumberReminder {
                return NumberReminder(divisionBase, remainder * number)
            }

            fun sqr(): NumberReminder {
                return NumberReminder(divisionBase, remainder * remainder)
            }
        }

        class OptimizedNumber(private val reminders: Map<Int, NumberReminder>) :
            Number<OptimizedNumber> {
            override fun times(number: Int): OptimizedNumber =
                reminders.values.map { it.divisionBase to it * number }.toMap().let { OptimizedNumber(it) }

            override fun plus(number: Int): OptimizedNumber =
                reminders.values.map { it.divisionBase to it + number }.toMap().let { OptimizedNumber(it) }

            override fun divides(number: Int): Boolean = reminders[number]!!.remainder == 0

            override fun sqr(): OptimizedNumber =
                reminders.values.map { it.divisionBase to it.sqr() }.toMap().let { OptimizedNumber(it) }

            override fun div(number: Int): OptimizedNumber {
                TODO("Not yet implemented")
            }

            companion object {
                fun create(value: Int, dividers: List<Int>): OptimizedNumber {
                    return dividers.map { it to NumberReminder(it, value) }.toMap().let { OptimizedNumber(it) }
                }
            }
        }
    }


    class ExecutionMonkey<T : Number<T>>(
        val monkey: MonkeyCollection.Monkey,
        val items: MutableList<T>,
    ) {
        var operations = 0
        fun operate(item: T): T {
            operations++
            return when (monkey.operation) {
                is MonkeyCollection.Monkey.MonkeyOperation.Multiply -> item.times((monkey.operation as MonkeyCollection.Monkey.MonkeyOperation.Multiply).number)
                is MonkeyCollection.Monkey.MonkeyOperation.Sum -> item.plus((monkey.operation as MonkeyCollection.Monkey.MonkeyOperation.Sum).number)
                is MonkeyCollection.Monkey.MonkeyOperation.Square -> item.sqr()
            }
        }

        fun test(number: T): Boolean {
            return number.divides(monkey.testDivision)
        }

        companion object {
            fun optimizedExecutionMonkeys(monkeyCollection: MonkeyCollection): Map<Int, ExecutionMonkey<Number.OptimizedNumber>> {
                val allDivisors = monkeyCollection.monkeys.map { it.testDivision }.toList()
                return monkeyCollection.monkeys.map {
                    val monkey = it
                    it.startingItems
                        .map { Number.OptimizedNumber.create(it, allDivisors) }.toMutableList()
                        .let {
                            ExecutionMonkey(monkey, it)
                        }
                }.map { it.monkey.monkeyIndex to it }.toMap()
            }

            fun simpleExecutionMonkeys(monkeyCollection: MonkeyCollection): Map<Int, ExecutionMonkey<Number.SimpleNumber>> {
                return monkeyCollection.monkeys.map {
                    val monkey = it
                    it.startingItems
                        .map { Number.SimpleNumber(it) }.toMutableList()
                        .let {
                            ExecutionMonkey(monkey, it)
                        }
                }.map { it.monkey.monkeyIndex to it }.toMap()
            }
        }

    }

    override fun partOne(): Any {
        val monkeyCollection = MonkeyCollection(inputList)
        val simpleExecutionMonkeys = ExecutionMonkey.simpleExecutionMonkeys(monkeyCollection)
        for (i in 1..20) {
            for (executionMonkey in simpleExecutionMonkeys.values) {
                for (item in executionMonkey.items) {
                    val resultWorryLevel = executionMonkey.operate(item).div(3)
                    if (executionMonkey.test(resultWorryLevel)) {
                        simpleExecutionMonkeys[executionMonkey.monkey.ifTrueMonkey]?.items?.add(resultWorryLevel)
                    } else {
                        simpleExecutionMonkeys[executionMonkey.monkey.ifFalseMonkey]?.items?.add(resultWorryLevel)
                    }
                }
                executionMonkey.items.removeAll { true }
            }
        }
        return simpleExecutionMonkeys.values.map { it.operations }.sortedDescending().take(2)
            .reduce { a, b -> a * b }
    }


    override fun partTwo(): Any {
        val monkeyCollection = MonkeyCollection(inputList)
        val optimizeExecutionMonkeys = ExecutionMonkey.optimizedExecutionMonkeys(monkeyCollection)
        for (i in 1..10000) {
            for (executionMonkey in optimizeExecutionMonkeys.values) {
                for (item in executionMonkey.items) {
                    val resultWorryLevel = executionMonkey.operate(item)
                    if (executionMonkey.test(resultWorryLevel)) {
                        optimizeExecutionMonkeys[executionMonkey.monkey.ifTrueMonkey]?.items?.add(resultWorryLevel)
                    } else {
                        optimizeExecutionMonkeys[executionMonkey.monkey.ifFalseMonkey]?.items?.add(resultWorryLevel)
                    }
                }
                executionMonkey.items.removeAll { true }
            }
        }
        return optimizeExecutionMonkeys.values.map { it.operations }.sortedDescending().take(2)
            .map { it.toLong() } .reduce { a, b -> a * b }
    }
}