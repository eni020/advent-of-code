package common


enum class Direction(
    val xAxis: Int,
    val yAxis: Int
) {
    NORTH(-1, 0),
    EAST(0, 1),
    SOUTH(1, 0),
    WEST(0, -1),
    NORTHEAST(-1, 1),
    SOUTHEAST(1, 1),
    SOUTHWEST(1, -1),
    NORTHWEST(-1, -1);

    fun isNeighbour(position: Position, otherPosition: Position): Boolean {
        return position.row + xAxis == otherPosition.row
                && position.column + yAxis == otherPosition.column
    }

    fun turnRight(): Direction {
        return when (this) {
            EAST -> SOUTH
            SOUTH -> WEST
            WEST -> NORTH
            NORTH -> EAST
            else -> throw IllegalArgumentException("common.Direction not supported")
        }
    }
}
