package year2025.day9

import common.Position
import java.io.File
import kotlin.math.max
import kotlin.math.min
import kotlin.time.measureTime

fun main() {
    val timeTaken = measureTime {
        val redTiles = File("src/year2025/day9/input9.txt").readLines()
            .map { line ->
                val splitLine = line.split(",").map { it.toInt() }
                Position(splitLine[1], splitLine[0])
            }

        val horizontalLines = mutableSetOf<HorizontalLine>()
        val verticalLines = mutableSetOf<VerticalLine>()
        val rectangles = mutableListOf<Rectangle>()
        for (tile1Idx in redTiles.indices) {
            val tile1 = redTiles[tile1Idx]
            for (tile2Idx in tile1Idx + 1..redTiles.lastIndex) {
                val tile2 = redTiles[tile2Idx]
                if (tile1.hasSameRowWith(tile2)) {
                    horizontalLines.add(HorizontalLine(tile1, tile2))
                }
                if (tile1.hasSameColumnWith(tile2)) {
                    verticalLines.add(VerticalLine(tile1, tile2))
                }
                rectangles.add(createRectangle(tile1, tile2))
            }
        }

        var part2MaxArea: Long = 0
        rectangles.sortByDescending { it.area }
        for (rectangle in rectangles) {
            val outside = isOutside(rectangle, horizontalLines, verticalLines)
            if (!outside) {
                part2MaxArea = rectangle.area
                break
            }
        }

        println("Answer for Part One: ${rectangles.maxOf { it.area }}")
        println("Answer for Part Two: $part2MaxArea")
    }
    println(timeTaken)
}

fun createRectangle(tile1: Position, tile2: Position): Rectangle {
    val tiles = listOf(tile1, tile2)
    val minRow = tiles.minOf { it.row }
    val maxRow = tiles.maxOf { it.row }
    val minColumn = tiles.minOf { it.column }
    val maxColumn = tiles.maxOf { it.column }
    val topSide = HorizontalLine(minRow, minColumn..maxColumn)
    val bottomSide = HorizontalLine(maxRow, minColumn..maxColumn)
    val leftSide = VerticalLine(minColumn, minRow..maxRow)
    val rightSide = VerticalLine(maxColumn, minRow..maxRow)
    return Rectangle(topSide, bottomSide, leftSide, rightSide, tile1.getArea(tile2))
}


private fun isOutside(
    rectangle: Rectangle,
    horizontalLines: MutableSet<HorizontalLine>,
    verticalLines: MutableSet<VerticalLine>
): Boolean {
    val topCoveredRanges = mutableSetOf<Int>()
    val bottomCoveredRanges = mutableSetOf<Int>()
    for (horizontalLine in horizontalLines) {
        if (rectangle.getVerticalSides().any { isIntersected( horizontalLine, it) }) {
            return true
        }
        if (horizontalLine.row <= rectangle.topSide.row) {
            topCoveredRanges.addAll(horizontalLine.range)
        }

        if (horizontalLine.row >= rectangle.bottomSide.row) {
            bottomCoveredRanges.addAll(horizontalLine.range)
        }
    }
    if (rectangle.topSide.range.any { it !in topCoveredRanges }) {
        return true
    }
    if (rectangle.bottomSide.range.any { it !in bottomCoveredRanges }) {
        return true
    }

    val leftCoveredRanges = mutableSetOf<Int>()
    val rightCoveredRanges = mutableSetOf<Int>()
    for (verticalLine in verticalLines) {
        if (rectangle.getHorizontalSides().any { isIntersected( it, verticalLine) }) {
            return true
        }
        if (verticalLine.column <= rectangle.leftSide.column) {
            leftCoveredRanges.addAll(verticalLine.range)
        }

        if (verticalLine.column >= rectangle.rightSide.column) {
            rightCoveredRanges.addAll(verticalLine.range)
        }
    }
    if (rectangle.leftSide.range.any { it !in leftCoveredRanges }) {
        return true
    }
    if (rectangle.rightSide.range.any { it !in rightCoveredRanges }) {
        return true
    }
    return false
}

fun isIntersected(horizontalLine: HorizontalLine, verticalLine: VerticalLine): Boolean {
    return verticalLine.column in horizontalLine.range && verticalLine.getEndpoints().none { it.row == horizontalLine.row }
            && horizontalLine.row in verticalLine.range && horizontalLine.getEndpoints().none { it.column == verticalLine.column }
}

data class Rectangle (
    val topSide: HorizontalLine,
    val bottomSide: HorizontalLine,
    val leftSide: VerticalLine,
    val rightSide: VerticalLine,
    val area: Long
) {
    fun getHorizontalSides() = listOf(topSide, bottomSide)

    fun getVerticalSides() = listOf(leftSide, rightSide)
}

data class HorizontalLine (
    val row: Int,
    val range: IntRange
) {
    constructor(tile1: Position, tile2: Position):
            this(tile1.row, min(tile1.column, tile2.column)..max(tile1.column, tile2.column))

    fun getEndpoints(): List<Position> {
        return listOf(Position(row, range.first), Position(row, range.last))
    }
}

data class VerticalLine (
    val column: Int,
    val range: IntRange
) {
    constructor(tile1: Position, tile2: Position):
            this(tile1.column, min(tile1.row, tile2.row)..max(tile1.row, tile2.row))

    fun getEndpoints(): List<Position> {
        return listOf(Position( range.first, column), Position( range.last, column))
    }
}