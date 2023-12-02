import java.io.File

fun main() {
  var sumOfPossibleIdValues = 0
  var sumOfMinCubes = 0

  File("2023/day2/input2.txt").forEachLine { line ->
    val id = line.substringBefore(':').filter { it.isDigit() }.toInt()
    val rawSets = line.substringAfter(':').split(';')
    val sets = rawSets.map { rawSet ->
      SetOfCubes(rawSet)
    }
    val minRed = sets.maxOf { set -> set.red }
    val minGreen = sets.maxOf { set -> set.green }
    val minBlue = sets.maxOf { set -> set.blue }
    if (minRed <= 12 && minGreen <= 13 && minBlue <= 14) {
      sumOfPossibleIdValues += id
    }
    sumOfMinCubes += minRed * minGreen * minBlue

  }

  println("Answer for Part One: $sumOfPossibleIdValues")
  println("Answer for Part Two: $sumOfMinCubes")
}

private class SetOfCubes(set: String) {
  val red: Int
  val green: Int
  val blue: Int

  init {
    val setElements = set.split(',')
    val map: MutableMap<String, Int> = mutableMapOf()
    setElements.forEach{
      map[it.filter { it.isLetter() }] = it.filter { it.isDigit() }.toInt()
    }
    red = if (map.containsKey("red")) map["red"]!! else 0
    green = if (map.containsKey("green")) map["green"]!! else 0
    blue = if (map.containsKey("blue")) map["blue"]!! else 0
  }
}