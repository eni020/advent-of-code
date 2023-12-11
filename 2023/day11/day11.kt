package day11

import java.io.File

fun main() {
  val galaxies: MutableList<Pair<Int, Int>> = mutableListOf()

  var row = 0
  var maxColumn = 0
  File("2023/day11/input11.txt").forEachLine { line ->
    if (maxColumn == 0) {
      maxColumn = line.length - 1
    }
    for (i in line.indices) {
      if (line[i] == '#') {
        galaxies.add(Pair(row, i))
      }
    }
    ++row
  }

  val rowsWithGalaxies = galaxies.map { it.first }
  val columsWithGalaxies = galaxies.map { it.second }.distinct()
  val emptyRows = (0 until row).filter { !rowsWithGalaxies.contains(it) }
  val emptyColumns = (0 until row).filter { !columsWithGalaxies.contains(it) }

  var partOneSum = 0L
  var partTwoSum = 0L
  for (i in galaxies.indices) {
    for (j in i+1  until galaxies.size) {
      val verticalSteps = getPathLength(galaxies[i].first, galaxies[j].first, emptyRows)
      val horizontalSteps = getPathLength(galaxies[i].second, galaxies[j].second, emptyColumns)
      partOneSum += verticalSteps.first + horizontalSteps.first
      partTwoSum += verticalSteps.second + horizontalSteps.second
    }
  }

  println("Answer for Part One: $partOneSum")
  println("Answer for Part Two: $partTwoSum")
}

fun getPathLength(position1: Int, position2: Int, empties: List<Int>): Pair<Long, Long> {
  val smaller = if (position1 < position2) position1 else position2
  val bigger = if (position1 > position2) position1 else position2
  val rawPath = bigger - smaller
  val partOneSpace = empties.count { it in (smaller + 1)..<bigger }.toLong()
  val partTwoSpace = empties.count { it in (smaller + 1)..<bigger }.toLong() * 999999
  return Pair(rawPath + partOneSpace, rawPath + partTwoSpace)
}