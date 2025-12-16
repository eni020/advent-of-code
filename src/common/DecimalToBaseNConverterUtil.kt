package common

import kotlin.math.pow

class DecimalToBaseNConverterUtil {
    companion object {
        fun getBaseNRange(base: Int, maxLength: Int, start: Int = 0): List<String> {
            if (base > 10) {
                return listOf()
            }

            return (start..<base.toDouble().pow(maxLength).toInt()).map { getBaseNFromDecimal(base, it, maxLength) }.sortedBy { baseN -> baseN.filter { it =='1' }.length }
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
