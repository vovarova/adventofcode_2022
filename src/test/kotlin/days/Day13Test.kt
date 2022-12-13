package days

import days.Day13.ElementPair
import days.Day13.ElementsPairCollection
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day13Test {

    @Test
    fun listCheck() {
        check("[1,2,3]", "[1,6,1]", true)
        check("[1,2,3]", "[1,2,3]", true)
        check("[1,2,3]", "[1,6]", true)
        check("[1,2,3]", "[1,2]", false)
        check("[1,2,3]", "[1,2,3,1,1]", true)
        check("[2,2]", "[1,2,1,1]", false)
        check("[0]", "[2,1]", true)
        check("[2,0]", "[2]", false)
        check("[2,0]", "[2]", false)


        check("[[[]]]", "[1,1,3,1,1]", true)
        check("[1,1,3,1,1]", "[[[]]]", false)
    }


    fun check(left: String, right: String, result: Boolean) {
        val elementPair = ElementPair(
            ElementsPairCollection.parseElementCollection(left),
            ElementsPairCollection.parseElementCollection(right)
        )
        assertEquals(result, elementPair.inRightOrder(), "${left} ${right} : ${result}")
    }

    @Test
    fun parseInputs() {
        Day13().inputList.filter { it.isNotEmpty() }
            .forEach {
                val parseElementCollection =
                    ElementsPairCollection.parseElementCollection(it)
                assertEquals(it, parseElementCollection.toString())
            }

    }


    @Test
    fun part2Example() {
        val inputList = """
            [1,1,3,1,1]
            [1,1,5,1,1]

            [[1],[2,3,4]]
            [[1],4]

            [9]
            [[8,7,6]]

            [[4,4],4,4]
            [[4,4],4,4,4]

            [7,7,7,7]
            [7,7,7]

            []
            [3]

            [[[]]]
            [[]]

            [1,[2,[3,[4,[5,6,7]]]],8,9]
            [1,[2,[3,[4,[5,6,0]]]],8,9]
        """.trimIndent().lines()

        val addedEl6 =
            Day13.ElementCollection(mutableListOf(Day13.ElementCollection(mutableListOf(Day13.SingleElement(6)))))
        val addedEl2 =
            Day13.ElementCollection(mutableListOf(Day13.ElementCollection(mutableListOf(Day13.SingleElement(2)))))

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
        val result = (sortedElements.indexOf(addedEl6)+1) * (sortedElements.indexOf(addedEl2)+1)
        assertEquals(140, result)
    }

}