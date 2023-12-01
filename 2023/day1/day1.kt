import java.io.File

fun main() {
  var partOneSumOfCalibrationValues = 0
  var partTwoSumOfCalibrationValues = 0
  val letterDigitMap: Map<String, Int> = mapOf(
    "one" to 1,
    "two" to 2,
    "three" to 3,
    "four" to 4,
    "five" to 5,
    "six" to 6,
    "seven" to 7,
    "eight" to 8,
    "nine" to 9
  )

  File("src/main/resources/input1.txt").forEachLine { line ->
    val firstDigits = getFirstDigits(line, letterDigitMap)
    val partOneFirstDigit: Int = firstDigits.first!!
    val partTwoFirstDigit: Int = firstDigits.second!!
    val lastDigits = getFirstDigits(line.reversed(), letterDigitMap.mapKeys { it.key.reversed() })
    val partOneLastDigit: Int = lastDigits.first ?: partOneFirstDigit
    val partTwoLastDigit: Int = lastDigits.second ?: partTwoFirstDigit

    partOneSumOfCalibrationValues += partOneFirstDigit * 10 + partOneLastDigit
    partTwoSumOfCalibrationValues += partTwoFirstDigit * 10 + partTwoLastDigit
  }

  println("Answer for Part One: $partOneSumOfCalibrationValues")
  println("Answer for Part Two: $partTwoSumOfCalibrationValues")
}

private fun getFirstDigits(line: String, letterDigitMap: Map<String, Int>): Pair<Int?, Int?> {
  var firstDigit1: Int? = null
  var firstDigit2: Int? = null
  var endIdx = 1
  var startIdx = 0
  while (startIdx < line.length && firstDigit1 == null) {
    while (endIdx <= line.length && firstDigit1 == null) {
      val substring = line.substring(startIdx, endIdx)
      if (letterDigitMap.containsKey(substring) && firstDigit2 == null) {
        firstDigit2 = letterDigitMap[substring]!!.toInt()
        startIdx = endIdx
      } else if (substring.length == 1 && substring[0].isDigit()) {
        firstDigit1 = substring.toInt()
        if (firstDigit2 == null) {
          firstDigit2 = substring.toInt()
        }
        startIdx = endIdx
      }
      endIdx += 1
    }
    startIdx += 1
    endIdx = startIdx + 1
  }
  return Pair(firstDigit1, firstDigit2)
}