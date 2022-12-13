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

}