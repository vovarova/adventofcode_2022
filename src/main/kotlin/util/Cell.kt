package util

data class Cell(val row: Int, val column: Int) {
    fun up(): Cell = Cell(row + 1, column)
    fun down(): Cell = Cell(row - 1, column)
    fun left(): Cell = Cell(row, column - 1)
    fun right(): Cell = Cell(row, column + 1)
    fun toPair() = Pair(row, column)
}