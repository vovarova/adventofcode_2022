package days

import util.Cell
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue


class Day17 : Day(17) {

    /*    ####

        .#.
        ###
        .#.

        ..#
        ..#
        ###

        #
        #
        #
        #

        ##
        ##*/

    class ElementsBoard(val input: String) {
        val cells: MutableSet<Cell> = mutableSetOf()
        var moveIteration = -1
        var elementIteration = -1
        var leftBoard = 0
        var bottomBoard = 0
        var highestRow = bottomBoard
        var rightBoard = 8

        fun appearRow() = highestRow + 4
        fun appearColumn() = leftBoard + 3

        val elementQueue: List<() -> Element> =
            listOf({ horizontalLine() }, { plus() }, { L() }, { verticalLine() }, { square() })


        fun move(): Char {
            moveIteration++
            if (moveIteration == input.length) {
                moveIteration = 0
            }
            return input[moveIteration]
        }

        fun nextElement(): Element {
            elementIteration++
            if (elementIteration == elementQueue.size) {
                elementIteration = 0
            }
            return elementQueue[elementIteration].invoke()

        }

        fun addElementToRest(el: Element) {
            highestRow = Math.max(highestRow, el.maxRow)
            cells.removeIf { it.row < highestRow - 50 }
            cells.addAll(el.elements)
        }


        fun horizontalLine(): Element {
            return Element(appearRow(), IntRange(appearColumn(), appearColumn() + 3).map { column ->
                Cell(appearRow(), column)
            })
        }

        fun verticalLine(): Element {
            return Element(appearRow() + 3, IntRange(appearRow(), appearRow() + 3).map { row ->
                Cell(row, appearColumn())
            })
        }

        fun plus(): Element {
            val vertical = IntRange(appearRow(), appearRow() + 2).asSequence().map { row ->
                Cell(row, appearColumn() + 1)
            }
            val horizontal = IntRange(appearColumn(), appearColumn() + 2).asSequence().map { column ->
                Cell(appearRow() + 1, column)
            }
            return Element(appearRow() + 2, (vertical + horizontal).distinct().toList())
        }

        fun L(): Element {
            val vertical = IntRange(appearRow(), appearRow() + 2).asSequence().map { row ->
                Cell(row, appearColumn() + 2)
            }
            val horizontal = IntRange(appearColumn(), appearColumn() + 2).asSequence().map { column ->
                Cell(appearRow(), column)
            }
            return Element(appearRow() + 2, (vertical + horizontal).distinct().toList())
        }

        fun square(): Element {
            val elements = IntRange(appearRow(), appearRow() + 1).flatMap { row ->
                IntRange(appearColumn(), appearColumn() + 1).map { column ->
                    Cell(row, column)
                }
            }
            return Element(appearRow() + 1, elements)
        }

        fun boardToString(cellToVisualise: Set<Cell> = cells): String {
            val minRow = bottomBoard
            val maxRow = cellToVisualise.map { it.row }.maxOrNull() ?: 0
            val str: StringBuilder = StringBuilder()

            for (row in maxRow downTo minRow + 1) {
                str.append('|')
                for (column in leftBoard + 1..rightBoard - 1) {
                    if (cellToVisualise.contains(Cell(row, column))) {
                        str.append('#')
                    } else {
                        str.append('.')
                    }
                }
                str.append('|')
                str.appendLine()
            }
            IntRange(leftBoard, rightBoard).forEach { str.append('-') }
            return str.toString()
        }


        inner class Element(
            val maxRow: Int,
            val elements: List<Cell>
        ) {
            fun moveLeft(): Element {
                return move { it.left() }
            }

            fun moveDown(): Element {
                return move(maxRow - 1) { it.up() }
            }

            fun moveRight(): Element {
                return move { it.right() }
            }

            fun move(maxRowChange: Int = maxRow, function: (Cell) -> (Cell)): Element {
                val canDoMove = elements.asSequence().map(function).all {
                    (it.column in (leftBoard + 1) until rightBoard && it.row > bottomBoard) && !cells.contains(it)
                }
                return if (canDoMove) {
                    Element(maxRowChange, elements.map(function))
                } else {
                    this
                }
            }

            override fun toString(): String {
                return boardToString(elements.toSet() + cells)
            }
        }


    }


    @OptIn(ExperimentalTime::class)
    override fun partOne(): Any {
        val elementsBoard = ElementsBoard(inputString)
        measureTimedValue {
            for (i in 1..2022) {
                var nextElement = elementsBoard.nextElement()
                do {
                    val move = elementsBoard.move()
                    nextElement = if (move == '>') {
                        /* println("Right")*/
                        nextElement.moveRight()
                    } else if (move == '<') {
                        /*println("Left")*/
                        nextElement.moveLeft()
                    } else {
                        throw RuntimeException("Move not found ${move}")
                    }
                    /*println(nextElement)*/

                    val beforeDown = nextElement
                    /*println("Down")*/
                    nextElement = nextElement.moveDown()
                    /*println(nextElement)*/
                } while (beforeDown != nextElement)
                elementsBoard.addElementToRest(nextElement)
            }
        }.also { println("Duration ${it.duration.inWholeMilliseconds}") }
        return elementsBoard.highestRow
    }


    @OptIn(ExperimentalTime::class)
    override fun partTwo(): Any {
        val elementsBoard = ElementsBoard(inputString)
        for (i in 1..1000000000000) {

            var nextElement = elementsBoard.nextElement()
            do {

                val move = elementsBoard.move()
                nextElement = if (move == '>') {
                    /* println("Right")*/
                    nextElement.moveRight()
                } else if (move == '<') {
                    /*println("Left")*/
                    nextElement.moveLeft()
                } else {
                    throw RuntimeException("Move not found ${move}")
                }
                /*println(nextElement)*/

                val beforeDown = nextElement
                /*println("Down")*/
                nextElement = nextElement.moveDown()
                /*println(nextElement)*/

            } while (beforeDown != nextElement)
            elementsBoard.addElementToRest(nextElement)
            /*println(elementsBoard.boardToString())
        println("********************************")*/

        }

        return elementsBoard.highestRow
    }
}


