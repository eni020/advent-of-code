package year2025.day2

import java.io.File
import kotlin.math.max
import kotlin.math.min
import kotlin.time.measureTime

fun main() {
    val timeTaken = measureTime {
        var partOneSum: Long = 0
        var partTwoSum: Long = 0

        val inputLines = File("src/year2025/day2/input2.txt").readLines()
        val ranges: List<LongRange> = inputLines[0].split(',').map { s -> s.substringBefore('-').toLong()..s.substringAfter('-').toLong() }
        ranges.forEach { range ->
            val first = range.first
            val last = range.last
            var numStartRange = getFirstPart(first)..getFirstPart(last)
            if (numStartRange.first > numStartRange.last) {
                numStartRange = numStartRange.first..numStartRange.last * 10
            }

            val invalidIds: MutableSet<Long> = mutableSetOf()
            val numParts: MutableSet<String> = mutableSetOf()
            for (numStart in numStartRange) {
                val numPartString = numStart.toString()
                for (endIdx in numPartString.length downTo 1) {
                    val numPart = numPartString.substring(0, endIdx)
                    if (numParts.contains(numPart)) {
                        break
                    }
                    numParts.add(numPart)
                    val repeat = min(max(first.toString().length / endIdx, 2), last.toString().length)
                    if (repeat < 2) {
                        break
                    }
                    var numString = numPart.repeat(repeat)

                    if (numString.toLong() < first) {
                        numString += numPart
                    }

                    val num = numString.toLong()
                    if (num in range) {
                        if (numString == numPart + numPart) {
                            partOneSum += num
                        }
                        invalidIds.add(num)
                    }
                }
            }

            partTwoSum += invalidIds.sum()
        }

        println("Answer for Part One: $partOneSum")
        println("Answer for Part Two: $partTwoSum")
    }
    println(timeTaken)
}

private fun getFirstPart(num: Long): Long {
    val numString = num.toString()
    val halfLength = numString.length / 2
    val endIndex = if (halfLength != 0) halfLength else 1
    return numString.substring(0, endIndex).toLong()
}
