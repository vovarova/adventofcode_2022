package days

import java.util.*


class Day18 : Day(18) {

    data class Point2D(val x: Int, val y: Int)
    data class Point3D(val x: Int, val y: Int, val z: Int)
    class Cube(val aPoint3D: Point3D) {
        val coordinates: List<Point3D> = IntRange(aPoint3D.x - 1, aPoint3D.x).asSequence()
            .flatMap { x ->
                IntRange(aPoint3D.y - 1, aPoint3D.y).asSequence().flatMap { y ->
                    IntRange(aPoint3D.z - 1, aPoint3D.z).map { z ->

                        Point3D(x, y, z)
                    }
                }
            }.toList()

        val sideX = Side(coordinates.filter { it.x == aPoint3D.x }.toSet())
        val `sideX-1` = Side(coordinates.filter { it.x == aPoint3D.x - 1 }.toSet())
        val `sideY-1` = Side(coordinates.filter { it.y == aPoint3D.y - 1 }.toSet())
        val sideY = Side(coordinates.filter { it.y == aPoint3D.y }.toSet())
        val sideZ = Side(coordinates.filter { it.z == aPoint3D.z }.toSet())
        val `sideZ-1` = Side(coordinates.filter { it.z == aPoint3D.z - 1 }.toSet())

        val sides = setOf(sideX, `sideX-1`, sideY, `sideY-1`, sideZ, `sideZ-1`)

        data class Side(val aPoint3DS: Set<Point3D>)
    }

    class Cubes(intput: List<String>) {
        val cubeIndex: Map<Point3D, Cube> = intput.map {
            it.split(",").let {
                Cube(Point3D(it[0].toInt(), it[1].toInt(), it[2].toInt()))
            }
        }.map { it.aPoint3D to it }.toMap()
        val maxX = cubeIndex.values.flatMap { it.coordinates }.map { it.x }.max()
        val minX = cubeIndex.values.flatMap { it.coordinates }.map { it.x }.min()
        val maxY = cubeIndex.values.flatMap { it.coordinates }.map { it.y }.max()
        val minY = cubeIndex.values.flatMap { it.coordinates }.map { it.y }.min()
        val maxZ = cubeIndex.values.flatMap { it.coordinates }.map { it.z }.max()
        val minZ = cubeIndex.values.flatMap { it.coordinates }.map { it.z }.min()
    }

    override fun partOne(): Any {
        var square: Int = 0
        val cubes = Cubes(inputList)
        val cubesList = cubes.cubeIndex.values.toList()
        for (i in cubesList.indices) {
            square += cubesList[i].sides.size
            for (j in i + 1 until cubesList.size) {
                square -= cubesList[i].sides.intersect(cubesList[j].sides).size * 2
            }
        }
        return square
    }

    override fun partTwo(): Any {
        val cubes = Cubes(inputList)
        val visited = mutableSetOf<Point3D>()
        val empty = mutableSetOf<Cube>()
        val pointStack = LinkedList<Point3D>()
        pointStack.add(Point3D(cubes.maxX + 1, cubes.maxY + 1, cubes.maxZ + 1))
        while (pointStack.isNotEmpty()) {
            val poll = pointStack.poll()
            if (visited.contains(poll)) {
                continue
            }
            if (poll.x > cubes.maxX + 1 || poll.x < cubes.minX - 1
                || poll.y > cubes.maxY + 1 || poll.y < cubes.minY - 1
                || poll.z > cubes.maxY + 1 || poll.z < cubes.minZ - 1
            ) {
                continue
            }

            visited.add(poll)
            if (!cubes.cubeIndex.containsKey(poll)) {
                val element = Cube(poll)
                empty.add(element)
                pointStack.add(Point3D(poll.x - 1, poll.y, poll.z))
                pointStack.add(Point3D(poll.x + 1, poll.y, poll.z))
                pointStack.add(Point3D(poll.x, poll.y + 1, poll.z))
                pointStack.add(Point3D(poll.x, poll.y - 1, poll.z))
                pointStack.add(Point3D(poll.x, poll.y, poll.z + 1))
                pointStack.add(Point3D(poll.x, poll.y, poll.z - 1))
            }
        }
        return cubes.cubeIndex.values.flatMap { it.sides }.distinct().intersect(
            empty.flatMap { it.sides }.distinct()
        ).size
    }
}