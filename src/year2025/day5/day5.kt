package year2025.day5

import java.io.File
import kotlin.math.max
import kotlin.math.min
import kotlin.time.measureTime

fun main() {
    val timeTaken = measureTime {
        var partOneCount = 0

        var afterBlankLine = false
        val ranges = mutableListOf<LongRange>()
        File("src/year2025/day5/input5.txt").forEachLine { line ->
            if (afterBlankLine) {
                if (ranges.any { r -> line.toLong() in r }) {
                    partOneCount++
                }
            } else if (line == "") {
                afterBlankLine = true
            } else {
                ranges.add(line.substringBefore('-').toLong().. line.substringAfter('-').toLong())
            }
        }

        val uniqueRanges = mutableListOf<LongRange>()
        for (idx in ranges.indices) {
            var range = ranges[idx]
            var urIdx = 0
            while (urIdx in uniqueRanges.indices) {
                val uniqueRange = uniqueRanges[urIdx]
                if (listOf(range.first, range.last).any { it in uniqueRange }
                        || listOf(uniqueRange.first, uniqueRange.last).any { it in range }) {
                    range = min(range.first, uniqueRange.first)..max(range.last, uniqueRange.last)
                    uniqueRanges.remove(uniqueRange)
                } else {
                    urIdx++
                }
            }
            uniqueRanges.add(range)
        }


        val partTwoCount = uniqueRanges.sumOf { it.last - it.first + 1 }


        println("Answer for Part One: $partOneCount")
        println("Answer for Part Two: $partTwoCount")
    }
    println(timeTaken)
}
