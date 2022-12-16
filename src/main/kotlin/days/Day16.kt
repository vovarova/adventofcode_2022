package days

import java.util.*


class Day16 : Day(16) {

    class Valves(intput: List<String>) {
        inner class Valve(val name: String, val rate: Int, val lead: List<String>) {
            fun leads(): List<Valve> = lead.map { index[it]!! }
            override fun toString(): String {
                return "Valve(name='$name', rate=$rate, lead=$lead)"
            }
        }

        val index: Map<String, Valve>

        val orderedValves: List<Valve>

        val distance: Map<Valve, Map<Valve, Int>>

        init {
            val regExp =
                "Valve (?<valveName>.+?) has flow rate=(?<rate>\\d+); tunnel[s]? lead[s]? to valve[s]? (?<lead>.*)".toRegex()
            index = intput.map { line ->
                regExp.matchEntire(line)?.let {

                    Valve(
                        it.groups["valveName"]!!.value,
                        it.groups["rate"]!!.value.toInt(),
                        it.groups["lead"]!!.value.split(", ").toList()
                    )

                }
            }.also { it.forEach { println(it) } }.map { it!!.name to it }.toMap()
            orderedValves = index.values.filter { it.rate > 0 }.sortedByDescending { it.rate }
            distance = (orderedValves + index["AA"]!!).map { it to minDistance(it) }.toMap()
        }

        fun minDistance(from: Valve): MutableMap<Valve, Int> {
            val distance = mutableMapOf<Valve, Int>()
            val stack = PriorityQueue<Pair<Valve, Int>>(Comparator.comparing { it.second })
            stack.add(Pair(from, 0))
            while (stack.isNotEmpty()) {
                val poll = stack.poll()
                distance.put(poll.first, poll.second)
                poll.first.leads().filter { !distance.contains(it) }.forEach {
                    stack.add(Pair(it, poll.second + 1))
                }
            }
            return distance
        }

        fun initialPathPart1(): Path {
            return Path(listOf(index["AA"]!!), 30)
        }

        fun initialPathPart2(): Path {
            return Path(listOf(index["AA"]!!), 26)
        }

        inner class Path(val valves: List<Valves.Valve> = emptyList(), val time: Int) {
            fun addOpenedNode(valve: Valves.Valve): Path {
                return Path(valves + valve, time)
            }

            val potentialBenefit = run {
                var benefit = 0
                var timeLeft = timeLeft()
                val leftToOpen = orderedValves - valves.toSet()
                val minDistance = leftToOpen.map { distance[valves.last()]!![it]!! }.min()
                for (valve in leftToOpen) {
                    timeLeft -= minDistance + 1
                    benefit += valve.rate * Math.max(0, timeLeft)
                }
                pressure() + benefit
            }

            fun timeLeft(): Int {
                return time - valves.windowed(2).map {
                    distance[it[0]]!!.get(it[1])!! + 1
                }.sum()
            }

            fun pressure(): Long {
                var timeLeft = time
                var pressure = 0L
                valves.windowed(2).forEach {
                    val distance = distance[it[0]]!!.get(it[1])!!
                    timeLeft -= distance + 1
                    pressure += timeLeft * it[1].rate
                }
                return pressure
            }
        }

        companion object {
            fun potentialBenefit(leftTime: Int, leftToOpen: Set<Valve>): Long {
                var benefit = 0L
                var timeLeft = leftTime
                leftToOpen.windowed(2, step = 2) {
                    timeLeft -= 2
                    benefit += it[0].rate * Math.max(timeLeft, 0)
                    benefit += it[1].rate * Math.max(timeLeft, 0)
                }
                return benefit
            }
        }

    }

    override fun partOne(): Any {
        val valves = Valves(inputList)
        val nodesToOpen = valves.orderedValves.toMutableSet()
        var maxPresure = 1809L
        var maxPath = valves.initialPathPart1()
        val pathes: LinkedList<Valves.Path> = LinkedList<Valves.Path>().apply { addLast(valves.initialPathPart1()) }

        while (!pathes.isEmpty()) {
            val poll = pathes.pollLast()!!
            if (poll.potentialBenefit <= maxPresure) {
                continue
            }
            if (poll.pressure() > maxPresure)
                if (poll.pressure() > maxPresure) {
                    maxPresure = poll.pressure()
                    maxPath = poll
                }
            val notOpened = nodesToOpen - poll.valves
            notOpened.map { poll.addOpenedNode(it) }.filter { it.timeLeft() >= 0 }.forEach {
                pathes.addLast(it)
            }
        }
        return maxPresure
    }


    override fun partTwo(): Any {
        val valves = Valves(inputList)
        val nodesToOpen = valves.orderedValves.toMutableSet()
        var maxPresure = 0L
        var maxPath = Pair(valves.initialPathPart2(), valves.initialPathPart2())
        val pathes: LinkedList<Pair<Valves.Path, Valves.Path>> =
            LinkedList<Pair<Valves.Path, Valves.Path>>().apply { add(maxPath) }

        while (!pathes.isEmpty()) {
            val poll = pathes.pollLast()!!
            // trick to remove most of useless nodes
            val potentialBenefit = Valves.potentialBenefit(
                Math.min(poll.first.timeLeft(), poll.second.timeLeft()),
                nodesToOpen - (poll.first.valves + poll.second.valves).toSet()
            )
            if (potentialBenefit + poll.first.pressure() + poll.second.pressure() < maxPresure) {
                continue
            }
            if (poll.first.pressure() + poll.second.pressure() > maxPresure) {
                maxPresure = poll.first.pressure() + poll.second.pressure()
                maxPath = poll
            }
            val openValves = nodesToOpen - (poll.first.valves + poll.second.valves)
            openValves.flatMap {
                if (poll.first.valves.last() == poll.second.valves.last()) {
                    listOf(
                        Pair(poll.first.addOpenedNode(it), poll.second)
                    )
                } else {
                    listOf(
                        Pair(poll.first.addOpenedNode(it), poll.second),
                        Pair(poll.first, poll.second.addOpenedNode(it))
                    )
                }
            }.filter { it.first.timeLeft() >= 0 }.filter { it.second.timeLeft() >= 0 }.forEach {
                pathes.addLast(it)
            }
        }
        println(maxPresure)
        return maxPresure
    }
}
