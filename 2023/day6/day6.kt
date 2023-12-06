import java.io.File

fun main() {
  var partOneWaysToWin = 1

  val inputLines = File("2023/day6/input6.txt").readLines()
  val partOneNumbers = inputLines.map { it.split(' ').filter { e -> e.toIntOrNull() != null }.map { it.toLong() } }
  for (idx in 0..<partOneNumbers[0].size) {
    partOneWaysToWin *= getWaysToWin(partOneNumbers[0][idx], partOneNumbers[1][idx])
  }
  val partTwoNumbers = inputLines.map { line -> line.filter { it.isDigit() }.toLong() }
  val partTwoWaysToWin = getWaysToWin(partTwoNumbers[0], partTwoNumbers[1])

  println("Answer for Part One: $partOneWaysToWin")
  println("Answer for Part Two: $partTwoWaysToWin")
}

private fun getWaysToWin(
  time: Long,
  distance: Long
): Int {
  var res = 0
  var hold = time/2

  while (hold <= time && hold * (time - hold) > distance) {
    res += 1
    hold += 1
  }

  hold = time/2 - 1
  while (hold >= 0 && hold * (time - hold) > distance) {
    res += 1
    hold -= 1
  }
  return res
}
