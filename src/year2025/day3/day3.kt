package year2025.day3

import java.io.File
import kotlin.math.max
import kotlin.time.measureTime

fun main() {
    val timeTaken = measureTime {
        var partOneSum: Long = 0
        var partTwoSum: Long = 0

        val inputLines = File("src/year2025/day3/input3.txt").readLines()
        for (line in inputLines) {
            partOneSum += getLargestJoltage(line, 2)
            partTwoSum += getLargestJoltage(line, 12)
        }

        println("Answer for Part One: $partOneSum")
        println("Answer for Part Two: $partTwoSum")
    }
    println(timeTaken)
}

private fun getLargestJoltage(line: String, joltageLength: Int): Long {
    var maxJoltage: MutableList<Int> = mutableListOf(line[0].toString().toInt())
    val offCount = line.length - joltageLength
    for (lineIdx in 1..<line.length) {
        val actDigit = line[lineIdx].toString().toInt()

        if (maxJoltage.size < joltageLength) {
            maxJoltage.add(actDigit)
        }
        val startIdx = max(lineIdx - offCount, 0)
        for (joltageIdx in startIdx..<maxJoltage.size) {
            if (maxJoltage[joltageIdx] < actDigit) {
                maxJoltage[joltageIdx] = actDigit
                maxJoltage = maxJoltage.take(joltageIdx + 1).toMutableList()
                break
            }
        }
    }

    return maxJoltage.joinToString("").toLong()
}
