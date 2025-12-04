package common

import kotlin.math.abs

data class Position(
    val row: Int,
    val column: Int
) {
    fun getNeighbourDirection(other: Position): Direction? {
        return Direction.entries.find { it.isNeighbour(this, other) }
    }

    fun isNeighbour(other: Position, distance: Int = 1): Boolean {
        return isNeighbourStraight(other, distance) || isNeighbourDiagonally(other, distance)
    }

    fun isNeighbourStraight(other: Position, distance: Int = 1): Boolean {
        return isSame(row, other.row) && isNeighbour(column, other.column, distance)
                || isSame(column, other.column) && isNeighbour(row, other.row, distance)
    }

    fun isNeighbourDiagonally(other: Position, distance: Int = 1): Boolean {
        return isNeighbour(row, other.row, distance) && isNeighbour(column, other.column, distance)
    }

    private fun isSame(num1: Int, num2: Int): Boolean {
        return num1 == num2
    }

    private fun isNeighbour(num1: Int, num2: Int, distance: Int): Boolean {
        return abs(num1 - num2) == distance
    }
}
