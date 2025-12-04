package year2025.day4

import common.Position
import java.io.File
import kotlin.time.measureTime

fun main() {
    val timeTaken = measureTime {
        var partOneCount = 0
        var partTwoCount = 0

        val rolls: MutableList<Position> = mutableListOf()
        val inputLines = File("src/year2025/day4/input4.txt").readLines()
        for (row in inputLines.indices) {
            for (column in inputLines[row].indices) {
                if (inputLines[row][column] == '@') {
                    rolls.add(Position(row, column))
                }
            }
        }

        var prevCount = 0
        while (prevCount == 0 || partTwoCount != prevCount) {
            val rollsToRemove: MutableList<Position> = mutableListOf()
            for (roll in rolls) {
                if (rolls.count { it -> roll.isNeighbour(it) } < 4) {
                    rollsToRemove.add(roll)
                }
            }
            if (partOneCount == 0) {
                partOneCount = rollsToRemove.size
            }
            prevCount = partTwoCount
            partTwoCount += rollsToRemove.size
            rolls.removeAll(rollsToRemove)
        }

        println("Answer for Part One: $partOneCount")
        println("Answer for Part Two: $partTwoCount")
    }
    println(timeTaken)
}
