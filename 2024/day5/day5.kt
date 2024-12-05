package day5

import java.io.File
import kotlin.math.ceil

fun main() {
    var resultPartOne = 0
    var resultPartTwo = 0
    var rule = true
    val rules: MutableList<Pair<Int, Int>> = mutableListOf()

    File("2024/day5/input5.txt").forEachLine { line ->
        if (rule) {
            rule = line.isNotEmpty()
            if (rule) rules.add(readRule(line))
        } else {
            val pagesToProduce = line.split(",").map { it.toInt() }
            val relevantRules = rules.filter { pagesToProduce.containsAll(it.toList()) }
            val correct = relevantRules.all { r -> pagesToProduce.indexOf(r.first) < pagesToProduce.indexOf(r.second) }
            if (correct) {
                resultPartOne += getMiddleElement(pagesToProduce)
            } else {
                resultPartTwo += calculatePartTwo(pagesToProduce, relevantRules)
            }
        }
    }

    println("Answer for Part One: $resultPartOne")
    println("Answer for Part Two: $resultPartTwo")
}

private fun readRule(line: String): Pair<Int, Int> {
    val (first, second) = line.split("|").map { it.toInt() }
    return Pair(first, second)
}

private fun calculatePartTwo(pagesToProduce: List<Int>, relevantRules: List<Pair<Int, Int>>): Int {
    val pagesToSort = pagesToProduce.toMutableList()
    var i = 0
    while (i < pagesToSort.size) {
        val page = pagesToSort[i]
        if (!relevantRules.any { it.second == page && pagesToSort.indexOf(it.first) >= i }) {
            i++
        } else {
            pagesToSort.remove(page)
            pagesToSort.add(page)
        }
    }
    return getMiddleElement(pagesToSort)
}

private fun getMiddleElement(pagesToProduce: List<Int>) =
    pagesToProduce[ceil((pagesToProduce.size / 2).toDouble()).toInt()]
