package days


class Day20 : Day(20) {

    class Moves(val moves: List<Long>) {
        val zeroIndex = moves.indexOf(0)

        fun toCustomLinkedList(): CustomLinkedList {
            val customLinkedList = CustomLinkedList(0, moves.first())
            IntRange(1, moves.size - 1).forEach {
                customLinkedList.add(it, moves[it])
            }
            customLinkedList.finish()
            return customLinkedList
        }
    }


    class CustomLinkedList(index: Int, val value: Long) {
        val head: Node = Node(index, value)
        val indexNodes: MutableMap<Int, CustomLinkedList.Node> = mutableMapOf(head.index to head)
        var size: Int = 1
        var last: Node = head

        fun add(index: Int, value: Long) {
            size++
            val node = Node(index, value, prev = last)
            last.next = node
            last = last.next!!
            indexNodes[index] = last
        }

        fun finish() {
            head.prev = last
            last.next = head
        }

        inner class Node(val index: Int, val value: Long, var next: Node? = null, var prev: Node? = null)

        fun find(node: Node, moves: Int): Node {
            val normalizedNode = moves % size
            val positiveNormalized = if (normalizedNode < 0) {
                size + normalizedNode - 1
            } else {
                normalizedNode
            }
            if (positiveNormalized == 0) {
                return node
            }
            var currentNode = node.next!!
            for (iter in 2..positiveNormalized) currentNode = currentNode.next!!
            return currentNode
        }

        fun findMoves(node: Node, moves: Long): Node {
            val normalizedNode = moves % (size - 1)
            val positiveNormalized = if (normalizedNode < 0) {
                size - 1 + normalizedNode
            } else {
                normalizedNode
            }
            if (positiveNormalized == 0L) {
                return node
            }
            var currentNode = node.next!!
            for (iter in 2..positiveNormalized) currentNode = currentNode.next!!
            return currentNode
        }


        fun values(nextNode: (Node) -> Node): MutableList<Long> {
            val result = mutableListOf<Long>()
            var currentNode = indexNodes[0]!!
            for (i in 1..size) {
                result.add(currentNode.value)
                currentNode = nextNode.invoke(currentNode)
            }
            return result
        }

        override fun toString(): String {
            val next = values { it.next!! }.joinToString()
            val prev = values { it.prev!! }.joinToString()
            return "Next: ${next} .Prev: ${prev}"
        }

        fun move(index: Int) {
            val node = indexNodes[index]!!
            val nodeToReplace = findMoves(node, node.value)
            if (nodeToReplace == node) return
            //prev node next
            node.prev!!.next = node.next
            node.next!!.prev = node.prev

            node.next = nodeToReplace.next
            node.prev = nodeToReplace
            nodeToReplace.next!!.prev = node
            nodeToReplace.next = node
        }
    }


    override fun partOne(): Any {
        val moves = Moves(inputList.map { it.toLong() })
        val customLinkedList = moves.toCustomLinkedList()
        for (i in 0 until customLinkedList.size) {
            customLinkedList.move(i)
        }
        val node = customLinkedList.indexNodes[moves.zeroIndex]!!
        val `1000` = customLinkedList.find(node, 1000).value
        val `2000` = customLinkedList.find(node, 2000).value
        val `3000` = customLinkedList.find(node, 3000).value
        println("1000:${`1000`},2000:${`2000`},3000:${`3000`}")
        return `1000` + `2000` + `3000`
    }

    override fun partTwo(): Any {
        val moves = Moves(inputList.map { it.toLong() }.map { it * 811589153 })
        val customLinkedList = moves.toCustomLinkedList()
        for (iter in 1..10) {
            for (i in 0 until customLinkedList.size) {
                customLinkedList.move(i)
            }
        }
        val node = customLinkedList.indexNodes[moves.zeroIndex]!!
        val `1000` = customLinkedList.find(node, 1000).value
        val `2000` = customLinkedList.find(node, 2000).value
        val `3000` = customLinkedList.find(node, 3000).value
        println("1000:${`1000`},2000:${`2000`},3000:${`3000`}")
        return `1000` + `2000` + `3000`
    }
}
