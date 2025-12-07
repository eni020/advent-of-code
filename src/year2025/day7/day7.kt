package year2025.day7

import java.io.File
import kotlin.time.measureTime

fun main() {
    val timeTaken = measureTime {
        var partOneCount = 0

        val grid = File("src/year2025/day7/input7.txt").readLines().map { it.toList() }.toMutableList()

        var visitedInPreviousLine = mapOf(grid[0].indexOf('S') to 1.toLong())
        for (rowIdx in 1..grid.lastIndex) {
            if (grid[rowIdx].distinct().size == 1) {
                continue
            }
            val visitedInActLine = mutableMapOf<Int, Long>()
            val splitters = mutableSetOf<Int>()
            for (column in visitedInPreviousLine.keys) {
                val cnt = visitedInPreviousLine[column]!!
                if (grid[rowIdx][column] == '^') {
                    splitters.add(column)
                    visitedInActLine.compute(column - 1) { _, value -> (value ?: 0) + cnt }
                    visitedInActLine.compute(column + 1) { _, value -> (value ?: 0) + cnt }
                } else {
                    visitedInActLine.compute(column) { _, value -> (value ?: 0) + cnt }
                }
            }
            partOneCount += splitters.size
            visitedInPreviousLine = visitedInActLine
        }

        println("Answer for Part One: $partOneCount")
        println("Answer for Part Two: ${visitedInPreviousLine.values.sum()}")
    }
    println(timeTaken)
}
