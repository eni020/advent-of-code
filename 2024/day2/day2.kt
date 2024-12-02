package day2

import java.io.File

fun main() {
  var resultPartOne = 0
  var resultPartTwo = 0
  File("2024/day2/input2.txt").forEachLine { line ->
    var levels: List<Int> = line.split(" ").map { it.toInt() }

    if (levels[0] > levels[1]) {
      levels = levels.reversed()
    }

    if (isReportSafe(levels)) {
      resultPartOne++
      resultPartTwo++
    } else if (isToleratedReportSafe(levels) || isToleratedReportSafe(levels.reversed())) {
      resultPartTwo++
    }
  }
  println("Answer for Part One: $resultPartOne")
  println("Answer for Part Two: $resultPartTwo")
}

private fun isReportSafe(levels: List<Int>): Boolean {
  for (i in 1 until levels.size) {
    val diff = levels[i] - levels[i - 1]
    if (isDiffUnsafe(diff)) {
      return false
    }
  }
  return true
}

private fun isToleratedReportSafe(levels: List<Int>): Boolean {
  val toleratedLevels = levels.toMutableList()
  for (i in 0 until levels.size - 1) {
    val diff = levels[i + 1] - levels[i]
    if (isDiffUnsafe(diff)) {
      if (i == levels.size - 2 || !isDiffUnsafe(levels[i + 2] - levels[i])) {
        toleratedLevels.removeAt(i + 1)
      } else {
        toleratedLevels.removeAt(i)
      }
      return isReportSafe(toleratedLevels)
    }
  }
  return true
}

private fun isDiffUnsafe(diff: Int) = diff < 1 || diff > 3
