package days

import java.util.*


class Day13() : Day(13) {

    sealed interface Element {
        fun toCollection(): ElementCollection

        data class SingleElement(val value: Int) : Element {
            override fun toCollection(): ElementCollection = ElementCollection(mutableListOf(this))
            override fun toString(): String {
                return value.toString()
            }
        }

        data class ElementCollection(val elements: MutableList<Element> = mutableListOf()) : Element {
            override fun toCollection(): ElementCollection = this

            override fun toString(): String {
                return "[" + elements.map { it.toString() }.joinToString(separator = ",") + "]"
            }
        }
    }


    data class ElementPair(val left: Element, val right: Element) {

        fun inRightOrder(): Boolean {
            return comparison(left.toCollection(), right.toCollection()) != ComparisonResult.INVALID_ORDER
        }

        fun inRightOrderNonRecursive(): Boolean {
            return comparisonNonRecursive(left.toCollection(), right.toCollection())
        }

        enum class ComparisonResult {
            RIGHT_ORDER,
            INVALID_ORDER,
            CONTINUE

        }


        fun comparisonNonRecursive(
            left: Element.ElementCollection,
            right: Element.ElementCollection
        ): Boolean {
            val execStack = LinkedList<Pair<Iterator<Element>, Iterator<Element>>>()
            execStack.add(Pair(left.elements.iterator(), right.elements.iterator()))

            while (execStack.isNotEmpty()) {
                val peek = execStack.peekLast()
                when (peek.first.hasNext() to peek.second.hasNext()) {
                    true to false -> return false
                    false to true -> return true
                    false to false -> {
                        execStack.pollLast()
                        continue
                    }
                }
                val leftElement = peek.first.next()
                val rightElement = peek.second.next()
                if ((leftElement is Element.SingleElement) && (rightElement is Element.SingleElement)) {
                    if (leftElement.value > rightElement.value) {
                        return false
                    } else if (leftElement.value < rightElement.value) {
                        return true
                    }
                } else {
                    execStack.addLast(
                        Pair(
                            leftElement.toCollection().elements.iterator(),
                            rightElement.toCollection().elements.iterator()
                        )
                    )
                }
            }
            return true
        }

        fun comparison(left: Element.ElementCollection, right: Element.ElementCollection): ComparisonResult {
            for (i in 0 until Math.min(left.elements.size, right.elements.size)) {
                val leftElement = left.elements[i]
                val rightElement = right.elements[i]
                if ((leftElement is Element.SingleElement) && (rightElement is Element.SingleElement)) {
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
            fun parseElementCollection(value: String): Element.ElementCollection {
                val rootCollection = Element.ElementCollection()
                val elementsStack = LinkedList<Element.ElementCollection>()
                    .also { it.add(rootCollection) }

                val tokenSplit = value
                    .replace("[", "-[-").replace("]", "-]-").split("-", ",")
                    .filter { it.isNotEmpty() }

                for (i in 1 until tokenSplit.size) {
                    when (tokenSplit[i]) {
                        "[" -> Element.ElementCollection().also { elementsStack.peekLast().elements.add(it) }
                            .also { elementsStack.add(it) }

                        "]" -> elementsStack.pollLast()
                        else -> elementsStack.peekLast().elements.add(Element.SingleElement(tokenSplit[i].toInt()))
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
        val addedEl6 =
            Element.ElementCollection(mutableListOf(Element.ElementCollection(mutableListOf(Element.SingleElement(6)))))
        val addedEl2 =
            Element.ElementCollection(mutableListOf(Element.ElementCollection(mutableListOf(Element.SingleElement(2)))))

        val elementsPairCollection = ElementsPairCollection(inputList)

        val sortedElements = elementsPairCollection.pairs.flatMap { listOf(it.left, it.right) }.toMutableList().apply {
            add(addedEl6)
            add(addedEl2)
        }.also {
            it.sortWith { el1, el2 ->
                ElementPair(el1, el2).inRightOrder().let {
                    if (it) {
                        -1
                    } else {
                        1
                    }
                }
            }
        }
        return (sortedElements.indexOf(addedEl6) + 1) * (sortedElements.indexOf(addedEl2) + 1)
    }

}
