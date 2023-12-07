import java.io.File

fun main() {
  val partOneStrengthOrder = "23456789TJQKA"
  val partTwoStrengthOrder = "J23456789TQKA"
  val partOneCardsByTypes: Map<Int, MutableMap<String, Int>> = initMap()
  val partTwoCardsByTypes: Map<Int, MutableMap<String, Int>> = initMap()

  File("2023/day7/input7.txt").forEachLine { line ->
    val lineElements = line.split(' ')
    val card = lineElements[0]
    val bid = lineElements[1].toInt()
    val partOneCardCounts: List<Int> = getPartOneCardCounts(card)
    val partOneMapId = getMapId(partOneCardCounts)
    partOneCardsByTypes[partOneMapId]!![card] = bid

    val partTwoCardCounts: List<Int> = getPartTwoCardCounts(card)
    val partTwoMapId = getMapId(partTwoCardCounts)
    partTwoCardsByTypes[partTwoMapId]!![card] = bid
  }
  val partOneTotalWinnings = getTotalWinnings(partOneCardsByTypes, partOneStrengthOrder)
  val partTwoTotalWinnings = getTotalWinnings(partTwoCardsByTypes, partTwoStrengthOrder)

  println("Answer for Part One: $partOneTotalWinnings")
  println("Answer for Part Two: $partTwoTotalWinnings")
}

private fun getTotalWinnings(cardsByTypes: Map<Int, MutableMap<String, Int>>, strengthOrder: String): Int {
  val bidsInOrder = cardsByTypes.map { (_, cards) ->
    cards.toSortedMap { x, y -> compare(x, y, strengthOrder) }.map { it.value }
  }.flatten()
  val totalWinnings = bidsInOrder.sumOf { it * (bidsInOrder.indexOf(it) + 1) }
  return totalWinnings
}

private fun getPartOneCardCounts(card: String): List<Int> {
  return card.groupingBy { it }.eachCount().values.sortedDescending()
}

private fun getPartTwoCardCounts(card: String): List<Int> {
  val cardCounts: MutableList<Int> =
    card.filter { it != 'J' }.groupingBy { it }.eachCount().values.sortedDescending().toMutableList()
  if (cardCounts.isEmpty()) {
    return listOf(5)
  }
  cardCounts[0] = cardCounts[0] + card.count { it == 'J' }
  return cardCounts
}

private fun initMap(): MutableMap<Int, MutableMap<String, Int>> =
  mutableMapOf(
    0 to mutableMapOf(),
    1 to mutableMapOf(),
    2 to mutableMapOf(),
    3 to mutableMapOf(),
    4 to mutableMapOf(),
    5 to mutableMapOf(),
    6 to mutableMapOf()
  )

private fun compare(card1: String, card2: String, strengthOrder: String): Int {
  var idx = 0
  while (idx < card1.length) {
    val res = strengthOrder.indexOf(card1[idx]).compareTo(strengthOrder.indexOf(card2[idx]))
    if (res != 0) {
      return res
    }
    idx += 1
  }
  return 0
}

private fun getMapId(cardCounts: List<Int>): Int {
  val mapId = when (cardCounts) {
    listOf(1, 1, 1, 1, 1) -> 0
    listOf(2, 1, 1, 1) -> 1
    listOf(2, 2, 1) -> 2
    listOf(3, 1, 1) -> 3
    listOf(3, 2) -> 4
    listOf(4, 1) -> 5
    listOf(5) -> 6
    else -> -1
  }
  return mapId
}