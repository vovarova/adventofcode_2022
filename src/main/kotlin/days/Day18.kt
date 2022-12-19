package days


class Day18 : Day(18) {

    data class Point(val x: Int, val y: Int, val z: Int)
    class Cube(val point: Point) {
        val coordinates: List<Point> = IntRange(point.x - 1, point.x).asSequence()
            .flatMap { x ->
                IntRange(point.y - 1, point.y).asSequence().flatMap { y ->
                    IntRange(point.z - 1, point.z).map { z ->

                        Point(x, y, z)
                    }
                }
            }.toList()

        val sideX = Side(coordinates.filter { it.x == point.x }.toSet())
        val `sideX-1` = Side(coordinates.filter { it.x == point.x - 1 }.toSet())
        val `sideY-1` = Side(coordinates.filter { it.y == point.y - 1 }.toSet())
        val sideY = Side(coordinates.filter { it.y == point.y }.toSet())
        val sideZ = Side(coordinates.filter { it.z == point.z }.toSet())
        val `sideZ-1` = Side(coordinates.filter { it.z == point.z - 1 }.toSet())

        val sides = setOf(sideX, `sideX-1`, sideY, `sideY-1`, sideZ, `sideZ-1`)

        data class Side(val points: Set<Point>)
    }

    class Cubes(intput: List<String>) {
        val cubesList: List<Cube> = intput.map {
            it.split(",").let {
                Cube(Point(it[0].toInt(), it[1].toInt(), it[2].toInt()))
            }
        }
    }

    override fun partOne(): Any {
        var square: Int = 0
        val cubes = Cubes(inputList)
        val cubesList = cubes.cubesList
        for (i in cubesList.indices) {
            square += cubesList[i].sides.size
            for (j in i + 1 until cubesList.size) {
                square -= cubesList[i].sides.intersect(cubesList[j].sides).size * 2
            }
        }
        return square
    }

    override fun partTwo(): Any {
        return ""
    }
}
