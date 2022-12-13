package days

import java.util.*


class Day13() : Day(13) {

    data class SingleElement(val value: Int) : Element {
        override fun toCollection(): ElementCollection = ElementCollection(mutableListOf(this))
        override fun toString(): String {
            return value.toString()
        }
    }

    data class ElementCollection(val elements: MutableList<Element> = mutableListOf()) : Element {
        override fun toCollection(): ElementCollection = this
        fun addSingleValues(vararg vals: Int) {
            vals.forEach { elements.add(SingleElement(it)) }
        }

        companion object {
            fun from(vararg vals: Int): ElementCollection {
                return ElementCollection().apply { addSingleValues(*vals) }
            }
        }

        override fun toString(): String {
            return "[" + elements.map { it.toString() }.joinToString(separator = ",") + "]"
        }
    }

    sealed interface Element {
        fun toCollection(): ElementCollection

    }


    data class ElementPair(val left: Element, val righ: Element) {

        fun inRightOrder(): Boolean {
            return comparison(left.toCollection(), righ.toCollection()) != ComparisonResult.INVALID_ORDER
        }

        enum class ComparisonResult {
            RIGHT_ORDER,
            INVALID_ORDER,
            CONTINUE

        }

        fun comparison(left: ElementCollection, right: ElementCollection): ComparisonResult {
            for (i in 0 until Math.min(left.elements.size, right.elements.size)) {
                val leftElement = left.elements[i]
                val rightElement = right.elements[i]
                if ((leftElement is SingleElement) && (rightElement is SingleElement)) {
                    if (leftElement.value > rightElement.value) {
                        return ComparisonResult.INVALID_ORDER
                    } else if (leftElement.value < rightElement.value) {
                        return ComparisonResult.RIGHT_ORDER
                    }
                } else {
                    when (comparison(leftElement.toCollection(), rightElement.toCollection())) {
                        ComparisonResult.INVALID_ORDER -> return ComparisonResult.INVALID_ORDER
                        ComparisonResult.CONTINUE -> {}
                        ComparisonResult.RIGHT_ORDER -> return ComparisonResult.RIGHT_ORDER
                    }
                }
            }

            return if (left.elements.size == right.elements.size) {
                ComparisonResult.CONTINUE
            } else if (left.elements.size > right.elements.size) {
                ComparisonResult.INVALID_ORDER
            } else {
                ComparisonResult.RIGHT_ORDER
            }
        }
    }


    class ElementsPairCollection(input: List<String>) {
        val pairs: MutableList<ElementPair> = mutableListOf()

        init {
            for (i in input.indices.step(3)) {
                val left = parseElementCollection(input[i])
                val right = parseElementCollection(input[i + 1])
                pairs.add(ElementPair(left, right))
            }
        }

        companion object {
            fun parseElementCollection(value: String): ElementCollection {
                val rootCollection = ElementCollection()
                val elementsStack = LinkedList<ElementCollection>()
                    .also { it.add(rootCollection) }

                val tokenSplit = value
                    .replace("[", "-[-").replace("]", "-]-").split("-", ",")
                    .filter { it.isNotEmpty() }

                for (i in 1 until tokenSplit.size) {
                    when (tokenSplit[i]) {
                        "[" -> ElementCollection().also { elementsStack.peekLast().elements.add(it) }
                            .also { elementsStack.add(it) }

                        "]" -> elementsStack.pollLast()
                        else -> elementsStack.peekLast().elements.add(SingleElement(tokenSplit[i].toInt()))
                    }
                }
                return rootCollection
            }
        }
    }


    override fun partOne(): Any {
        val elementsPairCollection = ElementsPairCollection(inputList)
        elementsPairCollection.pairs.forEach { println(it) }
        return elementsPairCollection.pairs.mapIndexed { i, pair ->
            i + 1 to pair.inRightOrder()
        }.filter { it.second }.map { it.first }.sum()
    }

    override fun partTwo(): Any {
        return ""
    }

}


fun main() {
    println(Day13().partOne())
}