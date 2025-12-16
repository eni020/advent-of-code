package year2025.day10

import common.DecimalToBaseNConverterUtil
import java.io.File
import kotlin.collections.all
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.time.measureTime

fun main() {
    val timeTaken = measureTime {
        var resultPartOne = 0
        var resultPartTwo = 0

        File("src/year2025/day10/input10.txt").readLines().forEach { line ->
            val buttons = mutableListOf<Set<Int>>()
            val indicatorLights = mutableListOf<Int>()
            val joltageRequirements = mutableListOf<Int>()
            parseInput(line, buttons, indicatorLights, joltageRequirements)

            val configs = mutableMapOf<List<Int>, Int>()
            for (configStr in DecimalToBaseNConverterUtil.getBaseNRange(2, buttons.size, 1)) {
                val flattenConfig = configStr.indices.filter { configStr[it] == '1' }.map { buttons[it] }.toSet().flatten()
                val countList = (0..<indicatorLights.size).map { idx -> flattenConfig.count { it == idx }}
                configs.putIfAbsent(countList, configStr.count { it == '1' })
            }
            val cache = mutableMapOf<List<Int>, MutableSet<Triple<List<Int>, Int, Int>>>()
            process(joltageRequirements, configs, cache)
            updateCache(cache)
            resultPartOne += configs.filterKeys { k -> k.map { it % 2 } == indicatorLights }.minOf { it.value }
            resultPartTwo += getCount(cache, joltageRequirements).min()
        }


        println("Answer for Part One: $resultPartOne")
        println("Answer for Part Two: $resultPartTwo")
    }
    println(timeTaken)
}

private fun process(
    requirements: List<Int>,
    configs: Map<List<Int>, Int>,
    cache: MutableMap<List<Int>, MutableSet<Triple<List<Int>, Int, Int>>>
) {
    if (requirements.all { it == 0 } || cache.containsKey(requirements)) {
        return
    }

    val pairs = getMatchingConfigs(requirements, configs, cache)
    for ((actRequirements, actConfigs) in pairs) {
        if (actConfigs.any { it == actRequirements }) {
            cache.putIfAbsent(actRequirements, mutableSetOf())
            cache[actRequirements]!!.add(Triple(actRequirements.map { 0 }, 0, 1))
            return
        }
        for ((configKey, configCount) in actConfigs) {
            val (nextGcd, newRequirements) = simplify(actRequirements.mapIndexed { idx, num -> num - (configKey[idx]) })
            val configCountToAdd = if (newRequirements.all { it == 0 }) configCount else configCount
            cache.putIfAbsent(actRequirements, mutableSetOf())
            cache[actRequirements]!!.add(Triple(newRequirements, nextGcd, configCountToAdd))
            val cacheSize = cache.size
            process(newRequirements, configs, cache)
            if (cacheSize >= cache.size && nextGcd > 2) {
                cache.putIfAbsent(actRequirements, mutableSetOf())
                val doubledRequirements = newRequirements.map { it * 2 }
                cache[actRequirements]!!.add(Triple(doubledRequirements, nextGcd / 2, configCountToAdd))
                process(doubledRequirements, configs, cache)
            }
        }
    }
}

fun getMatchingConfigs(
    requirements: List<Int>,
    configs: Map<List<Int>, Int>,
    cache: MutableMap<List<Int>, MutableSet<Triple<List<Int>, Int, Int>>>
): List<Pair<List<Int>, Map<List<Int>, Int>>> {
    val configsForOriginal = getMatchingConfigs(configs, requirements)
    val pairs = mutableListOf<Pair<List<Int>, Map<List<Int>, Int>>>()
    if (configsForOriginal.isNotEmpty()) {
        pairs.add(Pair(requirements, configsForOriginal))
    }

    val (gcd, simplifiedRequirements) = simplify(requirements)
    val pair = getConfigPair(configs, simplifiedRequirements, cache, requirements, gcd)
    if (pair != null) {
        pairs.add(pair)
    }
    return pairs
}

private fun getConfigPair(
    configs: Map<List<Int>, Int>,
    simplifiedRequirements: List<Int>,
    cache: MutableMap<List<Int>, MutableSet<Triple<List<Int>, Int, Int>>>,
    requirements: List<Int>,
    gcd: Int
): Pair<List<Int>, Map<List<Int>, Int>>? {
    if (simplifiedRequirements == requirements) {
        return null
    }
    val configsForSimplified = getMatchingConfigs(configs, simplifiedRequirements)
    if (configsForSimplified.isNotEmpty()) {
        cache.putIfAbsent(requirements, mutableSetOf())
        cache[requirements]!!.add(Triple(simplifiedRequirements, gcd, 0))
        return Pair(simplifiedRequirements, configsForSimplified)
    }
    return null
}

private fun getMatchingConfigs(
    configs: Map<List<Int>, Int>,
    requirements: List<Int>
): Map<List<Int>, Int> = configs.filterKeys { c ->
    c.mapIndexed { idx, num -> num % 2 == requirements[idx] % 2 && num <= requirements[idx] }.all { it }
}

private fun updateCache(
    cache: MutableMap<List<Int>, MutableSet<Triple<List<Int>, Int, Int>>>
) {
    for ((key, value) in cache) {
        value.removeIf { it.first == key }
    }
    var toRemove = cache.values.map { v -> v.map { it.first } }.flatten().filter { list -> !list.all { it == 0 } && !cache.containsKey(list) }.toSet()
    while (toRemove.isNotEmpty()) {
        val nextToRemove = mutableSetOf<List<Int>>()
        for (key in toRemove) {
            cache.remove(key)
            for (cacheValue in cache.values) {
                cacheValue.removeIf { it.first == key }
            }
            nextToRemove.addAll(cache.filterValues { it.isEmpty() }.keys)
        }
        toRemove = nextToRemove
    }

}

private fun simplify(joltageRequirements: List<Int>): Pair<Int, List<Int>> {
    val gcd = calcGcd(joltageRequirements.distinct())
    val simplifiedJoltageRequirements = joltageRequirements.map { it / gcd }
    return Pair(gcd, simplifiedJoltageRequirements)
}

private fun calcGcd(numbers: List<Int>): Int {
    var result = numbers[0]
    for (i in 1 until numbers.size) {
        var num1 = result
        var num2 = numbers[i]
        while (num2 != 0) {
            val temp = num2
            num2 = num1 % num2
            num1 = temp
        }
        result = num1
    }
    return if (result > 0) result else 1
}

private fun getCount(
    cache: MutableMap<List<Int>, MutableSet<Triple<List<Int>, Int, Int>>>,
    requirement: List<Int>
): List<Int> {
    if (requirement.all { it == 0 }) {
        return listOf(0)
    }

    val result = mutableListOf<Int>()
    for (config in cache[requirement]!!) {
        result.addAll(getCount(cache, config.first).map { it * config.second + config.third })
    }
    return result
}

private fun parseInput(
    line: String,
    buttons: MutableList<Set<Int>>,
    indicatorLights: MutableList<Int>,
    joltages: MutableList<Int>
) {
    val splitLine = line.split(" ").map { it.substring(1, it.length - 1) }
    for (idx in 1..<splitLine.lastIndex) {
        buttons.add(splitLine[idx].split(",").map { it.toInt() }.toSet())
    }

    val indicatorLightDiagram = splitLine[0]
    val joltageRequirement = splitLine[splitLine.lastIndex].split(",")
    for (idx in indicatorLightDiagram.indices) {
        indicatorLights.add(if (indicatorLightDiagram[idx] == '#') 1 else 0)
        joltages.add(joltageRequirement[idx].toInt())
    }
}