package year2023.day10

import java.io.File

fun main() {
  val mapOfTiles = mapOf(
    '|' to Direction(true, true,false, false),
    '-' to Direction(false, false, true, true),
    'L' to Direction(true, false, false, true),
    'J' to Direction(true, false, true, false),
    '7' to Direction(false, true, true, false),
    'F' to Direction(false, true, false, true),
    '.' to Direction(false, false, false, false),
    'S' to Direction(true, true, true, true)
  )

  val tiles: MutableList<MutableList<Direction>> = mutableListOf()
  var sRow = 0
  var sColumn = 0
  File("year2023/day10/input10.txt").forEachLine { line ->
    val actualRow: MutableList<Direction> = mutableListOf()
    line.indices.forEach { idx ->
      val tile = line[idx]
      actualRow.add(mapOfTiles[tile]!!)
      if(tile == 'S') {
        sRow = tiles.size
        sColumn = idx
      }
    }
    tiles.add(actualRow)
  }
  
  val visitedTiles: MutableList<Pair<Int, Int>> = mutableListOf()
  
  var tile = Pair(sRow, sColumn)
  var step = 0
  while (step == 0 || !visitedTiles.contains(tile)) {
    ++step
    visitedTiles.add(tile)
    tile = getNextTile(tiles, tile, visitedTiles)
  }
  val partOneRes = step/2

  val newS = getNewS(tiles, visitedTiles)
  tiles[sRow][sColumn] = mapOfTiles[newS]!!

  val partTwoSum = getPartTwoSum(visitedTiles, tiles)

  println("Answer for Part One: $partOneRes")
  println("Answer for Part Two: $partTwoSum")
}

private fun getPartTwoSum(
  visitedTiles: List<Pair<Int, Int>>,
  tiles: List<List<Direction>>
): Int {
  var partTwoSum = 0
  visitedTiles.sortedWith(compareBy({ it.first }, { it.second }))
  tiles.indices.forEach { row ->
    tiles[row].indices.forEach { column ->
      if (!visitedTiles.contains(Pair(row, column))) {
        val i = checkVerticalTiles(visitedTiles.filter { it.first < row && it.second == column }
          .map { tiles[it.first][it.second] })
        if (i) {
          ++partTwoSum
        }
      }
    }
  }
  return partTwoSum
}

private fun getNewS(
  tiles: List<List<Direction>>,
  visitedTiles: List<Pair<Int, Int>>
): Char {
  val first = tiles[visitedTiles[1].first][visitedTiles[1].second]
  val last = tiles[visitedTiles.last().first][visitedTiles.last().second]
  val newS = when {
    first.up && last.down || first.down && last.up -> '|'
    first.left && last.right || first.right && last.left -> '-'
    first.down && last.left || first.left && last.down -> 'L'
    first.down && last.right || first.right && last.down -> 'J'
    first.up && last.right || first.right && last.up -> '7'
    first.up && last.left || first.left && last.up -> 'F'
    else -> 'S'
  }
  return newS
}

private fun checkVerticalTiles(
  visitedTiles: List<Direction>,
): Boolean {
  if(visitedTiles.isEmpty()) {
    return false
  }
  val relevantTiles: MutableList<Direction> = mutableListOf()
  visitedTiles.forEach { dir ->
    if (dir.left || dir.right) {
      relevantTiles.add(dir)
    }
  }
  val lefties = relevantTiles.count { it.left } % 2
  val righties = relevantTiles.count { it.right } % 2
  return lefties == 1 && righties == 1
}

private fun getNextTile(tiles: List<List<Direction>>, tile: Pair<Int, Int>, visitedTiles: List<Pair<Int, Int>>): Pair<Int, Int> {
  val row = tile.first
  val column = tile.second
  
  val dir = tiles[row][column]
  val tileUp = Pair(row - 1, column)
  val tileDown = Pair(row + 1, column)
  val tileLeft = Pair(row, column - 1)
  val tileRight = Pair(row, column + 1)
  return when {
    row - 1 >= 0 && !visitedTiles.contains(tileUp) && dir.up && tiles[tileUp.first][tileUp.second].down -> tileUp
    row + 1 < tiles.size && !visitedTiles.contains(tileDown) && dir.down && tiles[tileDown.first][tileDown.second].up -> tileDown
    column - 1 >= 0 && !visitedTiles.contains(tileLeft) && dir.left && tiles[tileLeft.first][tileLeft.second].right -> tileLeft
    column + 1 < tiles[0].size && !visitedTiles.contains(tileRight) && dir.right && tiles[tileRight.first][tileRight.second].left -> tileRight
    else -> tile
  }
}

class Direction(val up: Boolean, val down: Boolean, val left: Boolean, val right: Boolean)

