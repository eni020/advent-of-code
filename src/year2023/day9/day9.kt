package year2023.day9

import java.io.File

fun main() {
  var partOneSum = 0
  var partTwoSum = 0

  File("year2023/day9/input9.txt").forEachLine { line ->
    var numbers = line.split(' ').map { it.toInt() }
    val lastNumbers = mutableListOf(numbers.last())
    val firstNumbers = mutableListOf(numbers.first())
    while (!numbers.all { it == 0 }) {
      val newNumbers: MutableList<Int> = mutableListOf()
      for (i in 0 until numbers.size - 1) {
        newNumbers.add(numbers[i + 1] - numbers[i])
      }
      lastNumbers.add(newNumbers.last())
      firstNumbers.add(newNumbers.first())
      numbers = newNumbers
    }
    partOneSum += calcInitialRowValue(lastNumbers, 1)
    partTwoSum += calcInitialRowValue(firstNumbers, -1)
  }

  println("Answer for Part One: $partOneSum")
  println("Answer for Part Two: $partTwoSum")
}

private fun calcInitialRowValue(nums: List<Int>, multiplier: Int): Int {
  val numbers = nums.toMutableList()
  for (i in numbers.size - 2 downTo 0) {
    numbers[i] = numbers[i] + (numbers[i + 1] * multiplier)
  }
  return numbers.first()
}