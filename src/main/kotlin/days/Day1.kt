package days

import java.util.*

class Day1 : Day(1) {

    override fun partOne(): Any {
        var maxCalorySum = 0
        var currentCalorySum = 0
        for (line in inputList) {
            if (line.isEmpty()) {
                maxCalorySum = Math.max(maxCalorySum, currentCalorySum)
                currentCalorySum = 0
            } else {
                currentCalorySum += line.toInt()
            }
        }
        return maxCalorySum
    }

    override fun partTwo(): Any {
        val priorityQueue = PriorityQueue<Int>()
        priorityQueue.add(0)
        priorityQueue.add(0)
        priorityQueue.add(0)
        var currentCalorySum = 0
        for (line in inputList) {
            if (line.isEmpty()) {
                val lowestElement = priorityQueue.peek()
                if (currentCalorySum > lowestElement) {
                    priorityQueue.poll()
                    priorityQueue.add(currentCalorySum)
                }
                currentCalorySum = 0
            } else {
                currentCalorySum += line.toInt()
            }
        }
        return priorityQueue.sum()
    }
}
