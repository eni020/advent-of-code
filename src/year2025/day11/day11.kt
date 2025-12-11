package year2025.day11

import java.io.File
import kotlin.time.measureTime

fun main() {
    val timeTaken = measureTime {
        var resultPartOne: Long
        var resultPartTwo: Long

        val deviceMap = mutableMapOf<String, Set<String>>()
        File("src/year2025/day11/input11.txt").readLines().forEach { line ->
            val splitLine = line.split(": ")
            deviceMap[splitLine.first()] = splitLine.last().split(" ").toMutableSet()
        }

        val devicePathCountMap = getPathCountMap(deviceMap, "out")
        val toFftPathCountMap = getPathCountMap(deviceMap, "fft")
        val toDacPathCountMap = getPathCountMap(deviceMap, "dac")

        resultPartOne = devicePathCountMap["you"]!!

        val fromFftToDac = listOf(toFftPathCountMap["svr"]!!, toDacPathCountMap["fft"]!!, devicePathCountMap["dac"]!!)
            .reduce { acc, num -> acc * num }
        val fromDacToFft = listOf(toDacPathCountMap["svr"]!!, toFftPathCountMap["dac"]!!, devicePathCountMap["fft"]!!)
            .reduce { acc, num -> acc * num }
        resultPartTwo = fromFftToDac + fromDacToFft


        println("Answer for Part One: $resultPartOne")
        println("Answer for Part Two: $resultPartTwo")
    }
    println(timeTaken)
}

private fun getPathCountMap(deviceMap: Map<String, Set<String>>, start: String): Map<String, Long> {
    val devices = deviceMap.filterKeys { it != start }
        .mapValues { e -> e.value.size }.entries
        .sortedBy { it.value }
        .map { it.key }.toMutableList()

    val devicePathCountMap = mutableMapOf("out" to 0.toLong(), start to 1.toLong())
    while (devices.isNotEmpty()) {
        val toRemove = mutableListOf<String>()
        for (device in devices) {
            val outputs = deviceMap[device]!!
            if (outputs.all { it in devicePathCountMap.keys }) {
                devicePathCountMap[device] = outputs.sumOf { devicePathCountMap[it]!! }
                toRemove.add(device)
            }
        }
        devices.removeAll(toRemove)
    }
    return listOf("you", "svr", "fft", "dac").associateWith { devicePathCountMap[it] ?: 0 }
}