package days

import java.util.*


class Day18 : Day(18) {

    data class Point2D(val x: Int, val y: Int)
    data class Point3D(val x: Int, val y: Int, val z: Int)
    class Cube(val point3D: Point3D) {
        val coordinates: List<Point3D> = IntRange(point3D.x - 1, point3D.x).asSequence()
            .flatMap { x ->
                IntRange(point3D.y - 1, point3D.y).asSequence().flatMap { y ->
                    IntRange(point3D.z - 1, point3D.z).map { z ->

                        Point3D(x, y, z)
                    }
                }
            }.toList()


        val sideX = SideV2(
            maxX = point3D.x,
            minX = point3D.x,
            maxY = point3D.y,
            minY = point3D.y - 1,
            maxZ = point3D.z,
            minZ = point3D.z - 1
        )
        val `sideX-1` = SideV2(
            maxX = point3D.x - 1,
            minX = point3D.x - 1,
            maxY = point3D.y,
            minY = point3D.y - 1,
            maxZ = point3D.z,
            minZ = point3D.z - 1
        )
        val `sideY` = SideV2(
            maxX = point3D.x,
            minX = point3D.x - 1,
            maxY = point3D.y,
            minY = point3D.y,
            maxZ = point3D.z,
            minZ = point3D.z - 1
        )
        val `sideY-1` = SideV2(
            maxX = point3D.x,
            minX = point3D.x - 1,
            maxY = point3D.y - 1,
            minY = point3D.y - 1,
            maxZ = point3D.z,
            minZ = point3D.z - 1
        )

        val `sideZ` = SideV2(
            maxX = point3D.x,
            minX = point3D.x - 1,
            maxY = point3D.y,
            minY = point3D.y - 1,
            maxZ = point3D.z,
            minZ = point3D.z
        )
        val `sideZ-1` = SideV2(
            maxX = point3D.x,
            minX = point3D.x - 1,
            maxY = point3D.y,
            minY = point3D.y - 1,
            maxZ = point3D.z - 1,
            minZ = point3D.z - 1
        )

        val sides = setOf(sideX, `sideX-1`, sideY, `sideY-1`, sideZ, `sideZ-1`)

        data class SideV2(
            val maxX: Int,
            val minX: Int,
            val maxY: Int,
            val minY: Int,
            val maxZ: Int,
            val minZ: Int
        )
    }


    class Cubes(intput: List<String>) {
        val cubeIndex: Map<Point3D, Cube> = intput.map {
            it.split(",").let {
                Cube(Point3D(it[0].toInt(), it[1].toInt(), it[2].toInt()))
            }
        }.map { it.point3D to it }.toMap()
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
        val cubesToCompare = mutableSetOf<Cube>()
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
                val currentCube = Cube(poll)
                empty.add(currentCube)
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

fun main() {
    println(Day18().partTwo())
}