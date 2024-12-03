package day3

import java.io.File

private const val REGEX = "mul\\((\\d+),(\\d+)\\)"

fun main() {
  val regex = Regex(REGEX)

  var enabled = true
  var resultPartOne = 0
  var resultPartTwo = 0
  File("2024/day3/input3.txt").forEachLine { line ->
    resultPartOne += matchMul(line, regex)
    line.split("do()").forEachIndexed { index, splittedLine ->
      if (index > 0) {
        enabled = true
      }
      if (enabled) {
        val (enabledLine, _) = splittedLine.split("don't()", limit = 2)
        resultPartTwo += matchMul(enabledLine, regex)
        if (splittedLine.contains("don't()")) {
          enabled = false
        }
      }
    }
  }

  println("Answer for Part One: $resultPartOne")
  println("Answer for Part Two: $resultPartTwo")
}

fun matchMul (line: String, regex: Regex): Int {
  var res = 0
  val matchResult = regex.findAll(line)
  matchResult.forEach {
    val (a, b) = it.destructured
    res += a.toInt() * b.toInt()
  }
  return res
}
