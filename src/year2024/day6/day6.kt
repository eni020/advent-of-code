package year2024.day6

import common.Direction
import common.Position
import java.io.File

fun main() {
    val obstructions: MutableList<Position> = mutableListOf()
    var guardStartPosition: Position? = null
    val lines = File("src/year2024/day6/input6.txt").readLines()
    val rownum = lines.size - 1
    val maxColumn = lines[0].length - 1
    for (row in lines.indices) {
        for (column in lines[row].indices) {
            val value = lines[row][column]
            val actPosition = Position(row, column)
            if (value == '#') {
                obstructions.add(actPosition)
            }
            if (value == '^') {
                guardStartPosition = actPosition
            }
        }
    }

    val stepsWithDirection: MutableList<Pair<Position, Direction>> = mutableListOf(guardStartPosition!! to Direction.NORTH)
    val maxPos = Position(rownum, maxColumn)
    isGuardStuck(stepsWithDirection, maxPos, obstructions)
    val resultPartOne = stepsWithDirection.map { it.first }.distinct().size
    val asd: MutableSet<Position> = mutableSetOf()
    stepsWithDirection.forEach { step ->
        if (step.first != stepsWithDirection.first().first) {
            val obs = obstructions.toMutableList()
            obs.add(step.first)
            if (isGuardStuck(mutableListOf(guardStartPosition to Direction.NORTH), maxPos, obs)) {
                asd.add(step.first)
            }
        }
    }
    val resultPartTwo = asd.size

    println("Answer for Part One: $resultPartOne")
    println("Answer for Part Two: $resultPartTwo")
}

private fun isGuardStuck(
    stepsWithDirection: MutableList<Pair<Position, Direction>>,
    maxPos: Position,
    obstructions: List<Position>
): Boolean {
    var direction = stepsWithDirection.last().second
    val maxRow = maxPos.row
    val maxColumn = maxPos.column
    var r = stepsWithDirection.last().first.row + direction.xAxis
    while (r in 0..maxRow) {
        var c = stepsWithDirection.last().first.column + direction.yAxis
        while (c in 0..maxColumn) {
            if (obstructions.contains(Position(r, c))) {
                r -= direction.xAxis
                c -= direction.yAxis
                direction = direction.turnRight()
                stepsWithDirection.removeLast()
            }
            val element = Pair(Position(r, c), direction)
            if (stepsWithDirection.contains(element)) {
                return true
            }
            stepsWithDirection.add(element)

            if (direction.yAxis == 0) {
                break
            }
            c += direction.yAxis
        }
        if (direction.xAxis == 0) {
            break
        }
        r += direction.xAxis
    }
    return false
}
