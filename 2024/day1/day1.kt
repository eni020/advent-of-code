import java.io.File
import kotlin.math.abs

fun main() {
  var left: MutableList<Int> = mutableListOf()
  var right: MutableList<Int> = mutableListOf()

  File("2024/day1/input1.txt").forEachLine { line ->
    val l = line.split("   ")
    left.add(l[0].toInt())
    right.add(l[1].toInt())
  }

  left = left.sorted().toMutableList()
  right = right.sorted().toMutableList()

  var resultPartOne = 0
  var resultPartTwo = 0
  val map: MutableMap<Int, Int> = mutableMapOf()
  for (i in 0 until left.size) {
    resultPartOne += abs(left[i]-right[i])
    if (!map.containsKey(left[i])) {
      map[left[i]] = right.filter { it == left[i] }.size
    }
    resultPartTwo += left[i] * map[left[i]]!!
  }

  println("Answer for Part One: $resultPartOne")
  println("Answer for Part Two: $resultPartTwo")
}
