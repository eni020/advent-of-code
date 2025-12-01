package year2025.day1

import java.io.File

fun main() {
    var position = 50
    var zeroPositionCount = 0
    var zeroClickCount = 0

    File("src/year2025/day1/input1.txt").forEachLine { line: String ->
        val direction: Int = if (line[0] == 'R') 1 else -1
        var step: Int = line.substring(1).toInt()
        if (step > 100) {
            val rotation = step / 100
            step %= 100
            zeroClickCount += rotation
        }

        val prevPosition = position
        position = position + (direction * step)

        if (position == 0 || position == 100) {
            position = 0
            zeroPositionCount++
            zeroClickCount++
        } else if (position < 0) {
            position = position + 100
            if (prevPosition != 0) {
                zeroClickCount++
            }
        } else if (position > 100) {
            position = position - 100
            zeroClickCount++
        }
    }


    println("Answer for Part One: $zeroPositionCount")
    println("Answer for Part Two: $zeroClickCount")
}
