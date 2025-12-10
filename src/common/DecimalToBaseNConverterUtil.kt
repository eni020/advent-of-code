package common

import kotlin.math.pow

class DecimalToBaseNConverterUtil {
    companion object {
        fun getBaseNRange(base: Int, maxLength: Int): List<List<Int>> {
            if (base > 10) {
                return listOf()
            }

            return (0..<base.toDouble().pow(maxLength).toInt()).map { getBaseNFromDecimal(base, it, maxLength).map { it.code - '0'.code } }.sortedBy { baseN -> baseN.sum() }
        }

        private fun getBaseNFromDecimal(base: Int, decimal: Int, padding: Int): String {
            var result = ""
            var d = decimal
            while (d > 0) {
                val mod = d % base
                result += mod
                d = (d - mod) / base
            }

            return result.reversed().padStart(padding, '0')
        }
    }

}
