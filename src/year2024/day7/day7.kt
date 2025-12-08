package year2024.day7

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
            var operatorDecimal = 0
            var result: Long = 0
            val operatorCount = operands.size - 1
            while (result != testResult && operatorDecimal < 2.toDouble().pow(operatorCount)) {
                val operators = getBase2FromDecimal(operatorDecimal, operatorCount)
                result = handleOperator(operators, operands)
                operatorDecimal++
            }
            if (result == testResult) {
                resultPartOne += testResult
            }

            result = 0
            operatorDecimal = 0
            while (result != testResult && operatorDecimal < 3.toDouble().pow(operatorCount)) {
                val operators = getBase3FromDecimal(operatorDecimal, operatorCount)
                result = handleOperator(operators, operands)
                operatorDecimal++
            }
            if (result == testResult) {
                resultPartTwo += testResult
            }
        }

        println("Answer for Part One: $resultPartOne")
        println("Answer for Part Two: $resultPartTwo")
    }
    println(timeTaken)
}

private fun handleOperator(
    operators: String,
    operands: List<Long>
): Long {
    var result = operands[0]
    for (operator in operators.withIndex()) {
        val actOperand = operands[operator.index + 1]
        when (operator.value) {
            '0' -> { result += actOperand }
            '1' -> { result *= actOperand }
            '2' -> { result = (result.toString() + actOperand.toString()).toLong() }
        }
    }
    return result
}

fun getBase2FromDecimal(decimal: Int, padding: Int): String {
    return getBasedNumberFromDecimal(2, decimal, padding)
}

fun getBase3FromDecimal(decimal: Int, padding: Int): String {
    return getBasedNumberFromDecimal(3, decimal, padding)
}

fun getBasedNumberFromDecimal(base: Int, decimal: Int, padding: Int): String {
    var result = ""
    var d = decimal
    while (d > 0) {
        val mod = d % base
        result += mod
        d = (d - mod) / base
    }
    return result.reversed().padStart(padding, '0')
}
