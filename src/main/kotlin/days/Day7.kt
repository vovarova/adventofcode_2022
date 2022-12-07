package days

import java.util.*

class Day7 : Day(7) {

    class FolderStructure(commands: List<String>) {
        val ROOT = "/"
        val index: MutableMap<String, Folder> = mutableMapOf()
        fun root(): Folder = index[ROOT]!!
        fun folders(): Collection<Folder> = index.values

        init {
            buildIndex(commands)
        }

        private fun buildIndex(commands: List<String>): Map<String, Folder> {
            val path: LinkedList<Folder> = LinkedList<Folder>()
            commands
                .forEach {
                    if (it.startsWith("$ cd ..")) {
                        path.pollLast()
                    } else if (it.startsWith("\$ cd")) {
                        val changedFolder = path.peekLast()?.childFolder(it.substring(5)) ?: Folder(ROOT)
                        path.add(changedFolder)
                    } else if (it.startsWith("dir")) {
                        path.peekLast()?.folders?.add(it.substring(4))
                    } else if (it[0].isDigit()) {
                        it.split(" ").also {
                            path.peekLast()?.files?.add(Pair(it[1], it[0].toInt()))
                        }
                    }
                }
            return index
        }

        inner class Folder(val path: String) {
            init {
                index[path] = this
            }

            private var calculated = false
            private var size: Long = 0
            val folders: MutableList<String> = mutableListOf()
            val files: MutableList<Pair<String, Int>> = mutableListOf()

            fun calcSize(): Long {
                if (!calculated) {
                    size = files.sumOf { it.second.toLong() } + folders.sumOf { index["$path$it/"]!!.calcSize() }
                    calculated = true
                }
                return size
            }

            fun childFolder(folderName: String): Folder {
                return Folder("$path$folderName/")
            }
        }


    }

    override fun partOne(): Any {
        val folderStructure = FolderStructure(inputList)
        return folderStructure.folders().map { it.calcSize() }.filter { it <= 100000 }.sum()
    }

    override fun partTwo(): Any {
        val folderStructure = FolderStructure(inputList)
        val unusedSpace = 70000000 - folderStructure.root().calcSize()
        val spaceNeeded = 30000000 - unusedSpace
        return folderStructure.folders().map { it.calcSize() }.filter { it >= spaceNeeded }.min()
    }

}