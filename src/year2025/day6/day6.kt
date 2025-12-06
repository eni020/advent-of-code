package year2025.day6

import java.io.File
import kotlin.collections.reduce
import kotlin.collections.sum
import kotlin.time.measureTime

fun main() {
    val timeTaken = measureTime {
        var partOneSum: Long = 0
        var partTwoSum: Long = 0

        val problems = mutableListOf<MutableList<Long>>()
        val inputLines = File("src/year2025/day6/input6.txt").readLines().toMutableList()

        for (line in inputLines) {
            val elements = line.split(' ').map { it.trim() }.filter { it.isNotBlank() }
            for (idx in elements.indices) {
                problems.add(mutableListOf())
                val element = elements[idx]
                val num = element.toLongOrNull()
                if (num != null) {
                    problems[idx].add(num)
                } else {
                    partOneSum += handleOperation(problems[idx], element)
                }
            }
        }

        val operandLine = inputLines.last()
        inputLines.remove(operandLine)
        var previousPosition = -1
        for (idx in operandLine.indices.reversed()) {
            if (operandLine[idx] != ' ') {
                val nums = mutableListOf<String>()
                for (line in inputLines) {
                    val endIdx = if (previousPosition == -1) line.length else previousPosition
                    val linePart = line.substring(idx, endIdx)
                    for (cIdx in linePart.indices) {
                        val char = linePart[cIdx].toString().trim()
                        if (nums.size <= cIdx) {
                            nums.add(char)
                        } else {
                            nums[cIdx] += char
                        }
                    }
                }
                partTwoSum += handleOperation(nums.filter { it.isNotBlank() }.map { it.toLong() }, operandLine[idx].toString())
                previousPosition = idx
            }
        }

        println("Answer for Part One: $partOneSum")
        println("Answer for Part Two: $partTwoSum")
    }
    println(timeTaken)
}

fun handleOperation(nums: List<Long>, operand: String): Long {
    if (operand == "+") {
        return nums.sum()
    } else if (operand == "*") {
        return nums.reduce { acc, num -> acc * num }
    }
    return 0
}