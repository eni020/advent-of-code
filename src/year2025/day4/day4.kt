package year2025.day4

import common.Position
import java.io.File
import kotlin.math.max
import kotlin.time.measureTime

fun main() {
    val timeTaken = measureTime {
        var partTwoCount = 0

        val grid: MutableList<MutableList<Char>> = mutableListOf()
        File("src/year2025/day4/input4.txt").forEachLine { line ->
            grid.add(line.toList().toMutableList())
        }

        val partOneRolls: MutableSet<Position> = mutableSetOf()

        var row = 0
        var column = 0
        while (row in grid.indices) {
            while (column in grid[row].indices) {
                if (grid[row][column] == '@') {
                    val position = Position(row, column)
                    val neighbours = position.getNeighbours(grid.lastIndex, grid[row].lastIndex)
                        .map { p -> grid[p.row][p.column] }
                    if (neighbours.count { char -> char in listOf('@', 'x') } < 4) {
                        partOneRolls.add(position)
                    }
                    if (neighbours.count { char -> char == '@' } < 4) {
                        partTwoCount++
                        grid[row][column] = 'x'
                        break
                    }
                }
                column++
            }
            if (column < grid.size && grid[row][column] == 'x') {
                column = max(column - 1, 0)
                row = max(row - 1, 0)
            } else {
                row++
                column = 0
            }
        }


        println("Answer for Part One: ${partOneRolls.size}")
        println("Answer for Part Two: $partTwoCount")
    }
    println(timeTaken)
}
