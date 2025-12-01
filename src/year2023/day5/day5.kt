package year2023.day5

import java.io.File

fun main() {
  var partOneElements: List<Long> = mutableListOf()
  var partOneMap: MutableMap<Long, Long> = mutableMapOf()
  var partTwoElements: MutableList<Pair<Long, Long>> = mutableListOf()
  var partTwoMap: MutableMap<Long, Pair<Long, Long>> = mutableMapOf()
  var mappings: MutableList<Mapping> = mutableListOf()

  val inputLines = File("year2023/day5/input5.txt").readLines()
  if (inputLines[0].contains(':')) {
    val seedNumbers = inputLines[0].substringAfter(':').trim().split(' ').map { it.toLong() }
    partOneElements = seedNumbers
    for (i in seedNumbers.indices step 2) {
      partTwoElements.add(Pair(seedNumbers[i], seedNumbers[i + 1]))
    }
  }

  (inputLines.drop(2) + "").forEach { line ->
    if (line.isEmpty()) {
      processPartOne(mappings, partOneElements, partOneMap)
      processPartTwo(mappings, partTwoElements, partTwoMap)

      partOneElements = partOneElements.map { partOneMap[it] ?: it }
      partTwoElements = partTwoElements.map { partTwoMap[it.first] ?: it }.toMutableList()
      partOneMap = mutableMapOf()
      partTwoMap = mutableMapOf()
      mappings = mutableListOf()
    } else if (!line.contains(':')) {
      mappings.add(Mapping(line.split(' ').map { it.toLong() }))
    }
  }

  val partOneLowestLocation = partOneElements.minOf { it }
  val partTwoLowestLocation = partTwoElements.minOf { it.first }

  println("Answer for Part One: $partOneLowestLocation")
  println("Answer for Part Two: $partTwoLowestLocation")
}

private fun processPartOne(
  mappings: MutableList<Mapping>,
  partOneElements: List<Long>,
  partOneMap: MutableMap<Long, Long>
) {
  partOneElements.forEach { source ->
    mappings.forEach { mapping ->
      val offset = getOffset(source, mapping)
      if (hasMapping(source, offset, mapping) && !partOneMap.containsKey(source)) {
        partOneMap[source] = getDestination(mapping, offset)
      }
    }
  }
}

private fun processPartTwo(
  mappings: MutableList<Mapping>,
  partTwoElements: MutableList<Pair<Long, Long>>,
  partTwoMap: MutableMap<Long, Pair<Long, Long>>
) {
  var idx = 0
  while (idx < partTwoElements.size) {
    val source = partTwoElements[idx]
    mappings.forEach { mapping ->
      val offset = getOffset(source.first, mapping)
      if (hasMapping(source.first, offset, mapping) && !partTwoMap.containsKey(source.first)) {
        val unmapped = mapping.sourceRangeStart + mapping.rangeLength
        val dest = getDestination(mapping, offset)
        if (unmapped >= source.first + source.second) {
          partTwoMap[source.first] = Pair(dest, source.second)
        } else {
          val newRange = unmapped - source.first
          partTwoMap[source.first] = Pair(dest, newRange)
          partTwoElements.add(Pair(unmapped, source.second - newRange))
        }
      }
    }
    idx += 1
  }
}

private fun getDestination(mapping: Mapping, offset: Long) = mapping.destRangeStart + offset

private fun getOffset(source: Long, mapping: Mapping) = source - mapping.sourceRangeStart

private fun hasMapping(source: Long, offset: Long, mapping: Mapping) =
  (source >= mapping.sourceRangeStart && offset < mapping.rangeLength)

class Mapping(elements: List<Long>) {
  val destRangeStart: Long
  val sourceRangeStart: Long
  val rangeLength: Long

  init {
    destRangeStart = elements[0]
    sourceRangeStart = elements[1]
    rangeLength = elements[2]
  }
}