package year2025.day12

import java.io.File
import kotlin.time.measureTime

fun main() {
    val timeTaken = measureTime {
        var resultPartOne: Int

        val inputLines = File("src/year2025/day12/input12.txt").readLines()
        val lastEmptyLine = inputLines.indices.last { inputLines[it].isEmpty() }

        val regions = mutableListOf<Pair<Region, Map<Int, Int>>>()
        for (lineIdx in lastEmptyLine + 1..inputLines.lastIndex) {
            val line = inputLines[lineIdx]
            val splitLine = line.split(": ")
            val width = splitLine[0].substringBefore('x').toInt()
            val length = splitLine[0].substringAfter('x').toInt()
            val presentMap = splitLine[1].split(' ').withIndex().associate { it.index to it.value.toInt() }
            regions.add(Pair(Region(width, length), presentMap))
        }

        resultPartOne = regions.count{ (region, presentMap) -> region.getArea() >= 9 * presentMap.values.sum()}

        println("Answer for Part One: $resultPartOne")
    }
    println(timeTaken)
}

data class Region(
    val width: Int,
    val length: Int
) {
    fun getArea(): Int {
        return width * length
    }
}