package day8

import java.io.File

fun main() {
  val map: MutableMap<String, Pair<String, String>> = mutableMapOf()

  val inputLines = File("2023/day8/input8.txt").readLines()
  val instructions = inputLines[0]

  (inputLines.drop(2)).forEach { line ->
    val lineElements = line.split(' ').map { it.filter { char -> char.isLetter() || char.isDigit() } }.filter { it != "" }
    map[lineElements[0]] = Pair(lineElements[1], lineElements[2])
  }

  var key = "AAA"
  var instrIdx = 0
  var partOneSteps = 0
  while (key != "ZZZ") {
    key = if (instructions[instrIdx] == 'L') map[key]!!.first else map[key]!!.second
    partOneSteps += 1
    instrIdx += 1
    if (instrIdx == instructions.length) {
      instrIdx = 0
    }
  }

  var keys: List<String> = map.keys.filter { it.endsWith('A') }.toMutableList()
  val steps: MutableList<Long> = mutableListOf()
  instrIdx = 0
  var partTwoSteps: Long = 1
  while (keys.isNotEmpty()) {
    val newKeys: MutableList<String> = mutableListOf()
    keys.forEach { k ->
      val newKey = if (instructions[instrIdx] == 'L') map[k]!!.first else map[k]!!.second
      if (newKey.endsWith('Z')) {
        steps.add(partTwoSteps)
      } else {
        newKeys.add(newKey)
      }
    }
    partTwoSteps += 1
    instrIdx += 1
    if (instrIdx == instructions.length) {
      instrIdx = 0
    }
    keys = newKeys
  }
  val partTwoResult = findLCMOfListOfNumbers(steps)

  println("Answer for Part One: $partOneSteps")
  println("Answer for Part Two: $partTwoResult")
}

fun findLCMOfListOfNumbers(numbers: List<Long>): Long {
  var result = numbers[0]
  numbers.indices.forEach { i ->
    result = findLCM(result, numbers[i])
  }
  return result
}

fun findLCM(n1: Long, n2: Long): Long {
  var gcd = 1
  var i = 1
  while (i <= n1 && i <= n2) {
    if (n1 % i == 0L && n2 % i == 0L) {
      gcd = i
    }
    ++i
  }

  return n1 * n2 / gcd
}