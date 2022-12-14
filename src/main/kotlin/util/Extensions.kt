package util

fun range(start: Int, end: Int): IntProgression {
    return if (start <= end) {
        IntProgression.fromClosedRange(start, end, 1)
    } else {
        IntProgression.fromClosedRange(start,end, -1)
    }
}