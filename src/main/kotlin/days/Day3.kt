package days

class Day3 : Day(3) {


    private fun priority(c: Char): Int {
        return if (c.isLowerCase()) {
            c.code - 'a'.code + 1
        } else {
            c.code - 'A'.code + 27
        }
    }

    override fun partOne(): Any {
        return inputList.map {
            var result = 0
            val halfSize = it.length / 2
            val charsSet = mutableSetOf<Char>()
            for (index in 0..halfSize - 1) {
                charsSet.add(it[index])
            }
            for (index in halfSize..it.length-1) {
                if (charsSet.contains(it[index])) {
                    result = priority(it[index])
                    break
                }
            }
            result
        }.sum()
    }


    override fun partTwo(): Any {
        return inputList.chunked( 3){
            it[0].toSet().intersect(it[1].toSet()).intersect(it[2].toSet()).first()
        }.map { priority(it) }.sum()
    }
}
