package days

import java.util.*

class Day7 : Day(7) {

    class FolderStructure(commands: List<String>) {
        val ROOT = "root"
        val index: MutableMap<String, Folder> = mutableMapOf()
        fun root(): Folder = index[ROOT]!!
        fun folders(): Collection<Folder> = index.values

        init {
            buildIndex(commands)
        }

        fun buildIndex(commands: List<String>): Map<String, Folder> {
            val path: LinkedList<String> = LinkedList<String>()
            path.add(ROOT)
            var currentFolder = Folder(ROOT)
            var ls = false
            commands.drop(1)
                .forEach {
                    if (it == "$ ls") {
                        ls = true
                    } else
                        if (it.startsWith("$ cd ..")) {
                            path.pollLast()
                            ls = false
                        } else if (it.startsWith("\$ cd")) {
                            path.add(it.substring(5))
                            val folderPath = path.joinToString(separator = "/")
                            currentFolder = Folder(folderPath)
                            index[folderPath] = currentFolder
                            ls = false
                        } else if (ls && it.startsWith("dir")) {
                            currentFolder.folders.add(it.substring(4))
                        } else if (ls) {
                            it.split(" ").also {
                                currentFolder.files.add(Pair(it[1], it[0].toInt()))
                            }
                        }
                }
            return index
        }

        inner class Folder(val name: String) {
            init {
                index[name] = this
            }
            private var calculated = false
            private var size: Long = 0
            val folders: MutableList<String> = mutableListOf()
            val files: MutableList<Pair<String, Int>> = mutableListOf()

            fun calcSize(): Long {
                if (!calculated) {
                    size = files.sumOf { it.second.toLong() } + folders.sumOf { index["$name/$it"]!!.calcSize() }
                    calculated = true
                }
                return size

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