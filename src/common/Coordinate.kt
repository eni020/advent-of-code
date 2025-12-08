package common

import kotlin.math.pow
import kotlin.math.sqrt

data class Coordinate(
    val x: Int,
    val y: Int,
    val z: Int,
) {
    fun getDistanceFrom(other: Coordinate): Double {
        return sqrt(getPow2(x, other.x) + getPow2(y, other.y) + getPow2(z, other.z))
    }

    private fun getPow2(value: Int, otherValue: Int): Double = (value - otherValue).toDouble().pow(2)
}
