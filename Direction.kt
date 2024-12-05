enum class Direction(
    private val xAxis: Int,
    private val yAxis: Int
) {
    HORIZONTAL(0, 1),
    VERTICAL(1, 0),
    DIAGONAL_TOP_LEFT(1, 1),
    DIAGONAL_TOP_RIGHT(1, -1);

    fun isNeighbour(position: Position, otherPosition: Position): Boolean {
        return position.row + xAxis == otherPosition.row
                && position.column + yAxis == otherPosition.column
    }
}
