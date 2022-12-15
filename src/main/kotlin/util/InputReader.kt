package util

import java.io.InputStreamReader

object InputReader {

    fun getInputAsString(day: Int): String {
        return fromResources(day).readText()
    }

    fun getInputAsList(day: Int): List<String> {
        return fromResources(day).readLines()
    }

    private fun fromResources(day: Int): InputStreamReader {
        return InputStreamReader(javaClass.classLoader.getResourceAsStream("input_day_$day.txt")!!)
    }
}
