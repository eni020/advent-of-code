package year2024.day7

import common.DecimalToBaseNConverterUtil
import java.io.File
import kotlin.math.pow
import kotlin.time.measureTime

fun main() {
    val timeTaken = measureTime {
        var resultPartOne: Long = 0
        var resultPartTwo: Long = 0
        val equations = File(
            "src/year2024/day7/input7.txt").readLines().map { line ->
            val splitLine = line.split(": ")
            splitLine[0].trim().toLong() to splitLine[1].split(" ").map { it.toLong() }
        }

        for (equation in equations) {
            val testResult = equation.first
            val operands = equation.second
            val operatorCount = operands.size - 1
            for (operatorSequence in DecimalToBaseNConverterUtil.getBaseNRange(2, operatorCount)) {
                val result = handleOperator(operatorSequence, operands)

                if (result == testResult) {
                    resultPartOne += testResult
                    break
                }
            }

            for (operatorSequence in DecimalToBaseNConverterUtil.getBaseNRange(3, operatorCount)) {
                val result = handleOperator(operatorSequence, operands)

                if (result == testResult) {
                    resultPartTwo += testResult
                    break
                }
            }
        }

        println("Answer for Part One: $resultPartOne")
        println("Answer for Part Two: $resultPartTwo")
    }
    println(timeTaken)
}

private fun handleOperator(
    operators: List<Int>,
    operands: List<Long>
): Long {
    var result = operands[0]
    for (operator in operators.withIndex()) {
        val actOperand = operands[operator.index + 1]
        when (operator.value) {
            0 -> { result += actOperand }
            1 -> { result *= actOperand }
            2 -> { result = (result.toString() + actOperand.toString()).toLong() }
        }
    }
    return result
}


