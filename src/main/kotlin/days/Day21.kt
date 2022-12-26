package days

import java.math.BigDecimal


class Day21 : Day(21) {


    class Operations(val inputs: List<String>) {
        val index: MutableMap<String, Operation>

        init {
            index = inputs.map {
                val initialSplit = it.split(": ")
                val monkeyName = initialSplit[0]
                val operation: Operation
                if (initialSplit[1].toLongOrNull() != null) {
                    operation = SingleNumber(initialSplit[1].toBigDecimal())
                } else {
                    val binaryOperationStr = initialSplit[1].split(" ")
                    val first = binaryOperationStr[0]
                    val second = binaryOperationStr[2]
                    when (binaryOperationStr[1]) {
                        "+" -> operation = BinaryOperation(first, second) { f, s -> f + s }
                        "-" -> operation = BinaryOperation(first, second) { f, s -> f - s }
                        "/" -> operation = BinaryOperation(first, second) { f, s -> f / s }
                        "*" -> operation = BinaryOperation(first, second) { f, s -> f * s }
                        else -> throw RuntimeException("Unknown operation ${binaryOperationStr[2]}")
                    }
                }
                monkeyName to operation
            }.toMap().toMutableMap()
        }

        interface Operation {
            fun value(): BigDecimal

        }

        class SingleNumber(val number: BigDecimal) : Operation {
            override fun value() = number
        }

        inner class BinaryOperation(
            val firsMonkey: String,
            val secondMonkey: String,
            val binaryOperator: (BigDecimal, BigDecimal) -> BigDecimal
        ) : Operation {
            override fun value() = binaryOperator.invoke(index[firsMonkey]!!.value(), index[secondMonkey]!!.value())
        }
    }

    override fun partOne(): Any {
        val operations = Operations(inputList)
        return operations.index["root"]!!.value()
    }

    class TestHumnValue(val operations: Operations) {
        val firsMonkeyUnknown: Boolean
        val root = operations.index["root"]!!.let { it as Operations.BinaryOperation }
        val firstMonkeyOperation = operations.index[root.firsMonkey]!!
        val secondMonkeyOperation = operations.index[root.secondMonkey]!!
        val knownMonkeyValue: BigDecimal

        init {
            var executed = false
            operations.index["humn"] = object : Operations.Operation {
                override fun value(): BigDecimal {
                    executed = true
                    return 0.toBigDecimal()
                }
            }
            firstMonkeyOperation.value()
            firsMonkeyUnknown = executed
            if (firsMonkeyUnknown) {
                knownMonkeyValue = secondMonkeyOperation.value()
            } else {
                knownMonkeyValue = firstMonkeyOperation.value()
            }
        }

        fun isIncreasing() = testValue(0) < testValue(Long.MAX_VALUE)
        fun testValue(humnValue: Long): BigDecimal {
            operations.index["humn"] = Operations.SingleNumber(humnValue.toBigDecimal())
            if (firsMonkeyUnknown) {
                return operations.index[root.firsMonkey]!!.value()
            } else {
                return operations.index[root.secondMonkey]!!.value()
            }
        }
    }

    override fun partTwo(): Any {
        val testHumnValue = TestHumnValue(Operations(inputList))
        var min = 0L
        var max = Long.MAX_VALUE
        val increasing = testHumnValue.isIncreasing()
        while (max >= min) {
            val middle = min + (max - min) / 2
            val testValue = testHumnValue.testValue(middle)
            if (testValue == testHumnValue.knownMonkeyValue) {
                return middle.toBigDecimal()
            } else if (increasing && testValue > testHumnValue.knownMonkeyValue) {
                max = middle - 1
            } else if (increasing && testValue < testHumnValue.knownMonkeyValue) {
                min = middle + 1
            } else if ((!increasing) && testValue > testHumnValue.knownMonkeyValue) {
                min = middle + 1
            } else if ((!increasing) && testValue < testHumnValue.knownMonkeyValue) {
                max = middle - 1
            }

            println("Min:${min} . Max:${max}")
        }
        return 0
    }
}