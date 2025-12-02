package year2025.day1

import java.io.File
import kotlin.time.measureTime

fun main() {
    val timeTaken = measureTime {
        var partOneSum: Long = 0
        var partTwoSum: Long = 0

        val inputLines = File("src/year2025/day2/input2.txt").readLines()
        val ranges: List<Pair<Long, Long>> = inputLines[0].split(',').map { s -> Pair(s.substringBefore('-').toLong(), s.substringAfter('-').toLong()) }
        ranges.forEach { (left, right) ->
            val leftStart = getFirstPart(left)
            var rightStart = getFirstPart(right)
            if (leftStart > rightStart) {
                rightStart = rightStart * 10
            }

            val invalidIds: MutableSet<Long> = mutableSetOf()
            for (numStart in leftStart..rightStart) {
                val numPartString = numStart.toString()
                for (i in 1..numPartString.length) {
                    val numPart = numPartString.substring(0, i)
                    var numString = ""
                    (1..(right.toString().length / numPart.length) + 1).forEach { j ->
                        numString += numPart
                        val num = numString.toLong()
                        if (j > 1 && num in left..right) {
                            if (j == 2) {
                                partOneSum += num
                            }
                            invalidIds.add(num)
                        }
                    }
                }
            }
            partTwoSum += invalidIds.sum()

            println()
        }

        println("Answer for Part One: $partOneSum")
        println("Answer for Part Two: $partTwoSum")
    }
    println(timeTaken)
}

private fun getFirstPart(num: Long): Long {
    val leftString = num.toString()
    val halfLength = leftString.length / 2
    val endIndex = if (halfLength != 0) halfLength else 1
    return leftString.substring(0, endIndex).toLong()
}
