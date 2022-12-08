package days

class Day8 : Day(8) {
    val treeMap = inputList.map { it.toCharArray().map { it.digitToInt() }.toList() }


    fun isVisible(rows: IntProgression, columns: IntProgression, compareValue: Int): Boolean {
        return rows.flatMap { row -> columns.map { row to it } }
            .all { treeMap[it.first][it.second] < compareValue }
    }

    override fun partOne(): Any {
        // Brute Force algorythm
        var visibleTree = treeMap.size * 4 - 4
        for (row in 1..treeMap.size - 2) {
            for (column in 1..treeMap.size - 2) {
                val currentCellValue = treeMap[row][column]
                val leftVisible =
                    isVisible(
                        rows = IntRange(row, row),
                        columns = IntRange(0, column - 1),
                        compareValue = currentCellValue
                    )

                val rightVisible =
                    isVisible(
                        rows = IntRange(row, row),
                        columns = IntRange(column + 1, treeMap.size - 1),
                        compareValue = currentCellValue
                    )

                val topVisible =
                    isVisible(
                        rows = IntRange(0, row - 1),
                        columns = IntRange(column, column),
                        compareValue = currentCellValue
                    )

                val bottomVisible =
                    isVisible(
                        rows = IntRange(row + 1, treeMap.size - 1),
                        columns = IntRange(column, column),
                        compareValue = currentCellValue
                    )
                if (leftVisible || rightVisible || topVisible || bottomVisible) {
                    visibleTree++
                }
            }
        }
        return visibleTree

    }


    fun calcView(rows: IntProgression, columns: IntProgression, compareValue: Int): Int {
        return rows.flatMap { row -> columns.map { row to it } }
            .takeWhile { treeMap[it.first][it.second] < compareValue }
            .let {
                if (it.isNotEmpty() && (it.last().second == 0 || it.last().first == 0
                            || it.last().second == treeMap.size - 1
                            || it.last().first == treeMap.size - 1)
                ) {
                    it.size
                } else {
                    it.size + 1
                }
            }
    }

    override fun partTwo(): Any {
        // Brute Force algorythm
        var maxView = 0
        for (row in 1..treeMap.size - 2) {
            for (column in 1..treeMap.size - 2) {
                val currentCellValue = treeMap[row][column]
                val leftVisible =
                    calcView(
                        rows = IntRange(row, row),
                        columns = IntRange(0, column - 1).reversed(),
                        compareValue = currentCellValue
                    )
                val rightVisible =
                    calcView(
                        rows = IntRange(row, row),
                        columns = IntRange(column + 1, treeMap.size - 1),
                        compareValue = currentCellValue
                    )
                val topVisible =
                    calcView(
                        rows = IntRange(0, row - 1).reversed(),
                        columns = IntRange(column, column),
                        currentCellValue
                    )
                val bottomVisible =
                    calcView(
                        rows = IntRange(row + 1, treeMap.size - 1),
                        columns = IntRange(column, column),
                        compareValue = currentCellValue
                    )
                maxView = Math.max(maxView, leftVisible * rightVisible * topVisible * bottomVisible)
            }
        }
        return maxView
    }

}