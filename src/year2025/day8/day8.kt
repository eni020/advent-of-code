package year2025.day8

import common.Coordinate
import java.io.File
import kotlin.time.measureTime

fun main() {
    val timeTaken = measureTime {
        val coordinates = File("src/year2025/day8/input8.txt").readLines()
            .map {
                val splitLine = it.split(",").map { it.toInt() }
                Coordinate(splitLine[0], splitLine[1], splitLine[2])
            }

        val connections = mutableListOf<Connection>()
        for (c1Indexed in coordinates.withIndex()) {
            val c1 = c1Indexed.value
            for (c2Idx in c1Indexed.index + 1..coordinates.lastIndex) {
                val c2 = coordinates[c2Idx]
                connections.add(Connection(c1, c2))
            }
        }
        connections.sortBy { it.distance }

        println("Answer for Part One: ${getPart1(connections.take(1000))}")
        println("Answer for Part Two: ${getPart2(connections, coordinates.size)}")
    }
    println(timeTaken)
}

fun getPart1(connections: List<Connection>): Int {
    val circuits = mutableListOf<MutableSet<Coordinate>>()
    for (connection in connections) {
        handleCircuitConnect(circuits, connection.getCoordinates())
    }
    return circuits.map { it.size }.sortedDescending().take(3).reduce { acc, size -> acc * size }
}

fun getPart2(connections: List<Connection>, jboxCount: Int): Long {
    val circuits = mutableListOf<MutableSet<Coordinate>>()
    for (connection in connections) {
        val coordinates = connection.getCoordinates()
        val circuitLength = handleCircuitConnect(circuits, coordinates)
        if (circuitLength == jboxCount) {
            return coordinates.map { it.x.toLong() }.reduce { acc, x -> acc * x }
        }
    }
    return 0
}

private fun handleCircuitConnect(
    circuits: MutableList<MutableSet<Coordinate>>,
    coordinates: Set<Coordinate>
): Int {
    val connectableCircuits = circuits.filter { circuit -> coordinates.any { coordinate -> coordinate in circuit } }
    if (connectableCircuits.size == 2) {
        connectableCircuits[0].addAll(connectableCircuits[1])
        circuits.remove(connectableCircuits[1])
    } else if (connectableCircuits.isNotEmpty()) {
        connectableCircuits[0].addAll(coordinates)
    } else {
        circuits.add(coordinates.toMutableSet())
        return 2
    }
    return connectableCircuits[0].size
}


data class Connection (
    val coordinate1: Coordinate,
    val coordinate2: Coordinate,
    val distance: Double,
) {
    constructor(coordinate1: Coordinate, coordinate2: Coordinate):
        this(coordinate1, coordinate2, coordinate1.getDistanceFrom(coordinate2))

    fun getCoordinates(): Set<Coordinate> {
        return setOf(coordinate1, coordinate2)
    }
}