import java.io.File

fun main() {
  val adjacentNumbers: MutableSet<Number> = mutableSetOf()
  var sumOfGearRatios = 0
  var previousNumbers: List<Number> = emptyList()
  var previousSymbols: List<Symbol> = emptyList()
  var previousGears: Map<Symbol, MutableSet<Number>> = mutableMapOf()
  var rowNum = 0
  File("2023/day3/input3.txt").forEachLine { line ->
    val actualNumbers: MutableList<Number> = mutableListOf()
    val actualSymbols: MutableList<Symbol> = mutableListOf()
    val actualGears: MutableMap<Symbol, MutableSet<Number>> = mutableMapOf()
    collectNumbersAndSymbols(line, rowNum, actualNumbers, actualSymbols, actualGears)
    (previousNumbers + actualNumbers).forEach { n ->
      if ((previousSymbols + actualSymbols).any { s -> isAdjacent(s, n)}) {
        adjacentNumbers.add(n)
        previousGears.forEach { (gear, numbers) ->
          if (isAdjacent(gear, n)) {
            numbers.add(n)
          }
        }
        actualGears.forEach { (gear, numbers) ->
          if (isAdjacent(gear, n)) {
            numbers.add(n)
          }
        }
      }
    }
    
    sumOfGearRatios += previousGears.values.filter { it.size > 1 }.sumOf { it.map { v -> v.value }.reduce(Int::times) }
    previousNumbers = actualNumbers
    previousSymbols = actualSymbols
    previousGears = actualGears

    rowNum += 1
  }
  val sumOfNumbers = adjacentNumbers.sumOf { it.value }

  println("Answer for Part One: $sumOfNumbers")
  println("Answer for Part Two: $sumOfGearRatios")
}

private fun collectNumbersAndSymbols(
  line: String, rowNum: Int,
  actualNumbers: MutableList<Number>,
  actualSymbols: MutableList<Symbol>,
  actualGears: MutableMap<Symbol, MutableSet<Number>>
) {
  var endIdx = 1
  var startIdx = 0
  while (startIdx < line.length) {
    while (endIdx <= line.length) {
      val substring = line.substring(startIdx, endIdx)
      if (substring.toIntOrNull() != null && (endIdx == line.length || !line[endIdx].isDigit())) {
        actualNumbers.add(Number(rowNum, startIdx, endIdx - 1, substring.toInt()))
        startIdx = endIdx
      } else if (substring.length == 1 && !substring.any { it.isDigit() || it == '.' }) {
        val symbol = Symbol(rowNum, endIdx - 1)
        if (substring[0] == '*') {
          actualGears[symbol] = mutableSetOf()
        }
        actualSymbols.add(symbol)
        startIdx = endIdx
      }
      endIdx += 1
    }
    startIdx += 1
    endIdx = startIdx + 1
  }
}

private fun isAdjacent(s: Symbol, n: Number) = ((s.row <= n.row + 1 && s.row >= n.row - 1)
    && (s.column >= n.columnStart - 1 && s.column <= n.columnEnd + 1))

private class Number(val row: Int, val columnStart: Int, val columnEnd: Int, val value: Int) {
}

private class Symbol(val row: Int, val column: Int) {
}