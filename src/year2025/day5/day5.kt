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

        for (idx in ranges.indices.reversed()) {
            val actRange = ranges[idx]
            for (rIdx in ranges.indices) {
                val rangeToCheck = ranges[rIdx]
                if (rIdx != idx && (listOf(actRange.first, actRange.last).any { it in rangeToCheck })) {
                    ranges[rIdx] = min(actRange.first, rangeToCheck.first)..max(actRange.last, rangeToCheck.last)
                    ranges.remove(actRange)
                    break
                }
            }
        }


        val partTwoCount = ranges.sumOf { it -> it.last - it.first + 1 }


        println("Answer for Part One: $partOneCount")
        println("Answer for Part Two: $partTwoCount")
    }
    println(timeTaken)
}
