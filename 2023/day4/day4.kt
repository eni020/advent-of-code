import java.io.File
import kotlin.math.pow

fun main() {
  var sumOfPoints = 0
  val winningCountByScratchcards: MutableMap<Int, Int> = mutableMapOf()
  val instancesByScratchcards: MutableMap<Int, Int> = mutableMapOf()
  File("2023/day4/input4.txt").forEachLine { line ->
    val lineParts = line.split(':')
    val id = lineParts[0].filter { it.isDigit() }.toInt()
    val numbers = lineParts[1].split('|').map { nums -> nums.split(' ').filter { it != "" }.map { it.toInt() } }
    val winnerNumbers: List<Int> = numbers[0]
    val numbersYouHave: List<Int> = numbers[1]
    val winningCount = numbersYouHave.count { winnerNumbers.contains(it) }
    sumOfPoints += 2.0.pow(winningCount - 1).toInt()
    if(winningCount > 0) {
      winningCountByScratchcards[id] = winningCount
    }
    instancesByScratchcards[id] = 1
  }
  winningCountByScratchcards.forEach {(id, winningCount) ->
    for (cardOffset in 1..winningCount) {
      val key = id + cardOffset
      if (instancesByScratchcards.containsKey(key)) {
        instancesByScratchcards[key] = instancesByScratchcards[key]!! + instancesByScratchcards[id]!!
      }
    }
  }

  val countOfScratchcards = instancesByScratchcards.values.sumOf { it }

  println("Answer for Part One: $sumOfPoints")
  println("Answer for Part Two: $countOfScratchcards")
}