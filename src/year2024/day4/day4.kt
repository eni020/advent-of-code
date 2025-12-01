package year2024.day4

import common.Direction
import common.Position
import java.io.File

fun main() {
    var resultPartOne = 0
    var resultPartTwo = 0
    var rownum = 0
    val wordSearches: MutableList<WordSearch> = mutableListOf()
    val startLetters: MutableList<WordSearchElement> = mutableListOf()
    val xWordSearches: MutableList<MutableList<WordSearchElement>> = mutableListOf()
    File("src/year2024/day4/input4.txt").forEachLine { line ->
        for (column in line.indices) {
            val letter = XmasLetter.valueOf(line[column].toString())
            val position = Position(rownum, column)
            handleWordSearchesWithDirection(wordSearches, letter, position)
            handleUndirectedWordSearches(letter, position, startLetters, wordSearches)

            if (letter != XmasLetter.X) {
                handleXWordSearches(xWordSearches, WordSearchElement(position, letter))
            }
        }
        val fullWords = wordSearches.filter { it.letter.isStartOrEnd() }
        resultPartOne += fullWords.size
        wordSearches.removeAll(fullWords)
        wordSearches.removeIf { w -> w.position.row < rownum || w.direction == Direction.EAST }
        startLetters.removeIf { w -> w.position.row < rownum }

        val fullXWords = xWordSearches.filter { x -> x.size == 5 }
        resultPartTwo += fullXWords.size
        xWordSearches.removeAll(fullXWords)
        xWordSearches.removeIf { x -> x.size == 1 || x.size == 4 }
        rownum++
    }
    println("Answer for Part One: $resultPartOne")
    println("Answer for Part Two: $resultPartTwo")
}

private fun handleUndirectedWordSearches(
    letter: XmasLetter,
    position: Position,
    undefinedDirectionWords: MutableList<WordSearchElement>,
    words: MutableList<WordSearch>
) {
    if (letter.isStartOrEnd()) {
        val actElement = WordSearchElement(position, letter)
        undefinedDirectionWords.add(actElement)
        return
    }
    undefinedDirectionWords.filter { w -> w.isNeighbourByLetter(letter) }.forEach { w ->
        val direction: Direction? = w.position.getNeighbourDirection(position)
        if (direction != null) {
            words.add(WordSearch(direction, if (letter == XmasLetter.M) 1 else -1, position, letter))
        }
    }
}

private fun handleWordSearchesWithDirection(
    wordSearches: MutableList<WordSearch>,
    letter: XmasLetter,
    position: Position
) {
    val foundWords: List<WordSearch> = wordSearches.filter { w -> w.isNeighbour(letter, position) }
    foundWords.forEach { w ->
        wordSearches.remove(w)
        wordSearches.add(WordSearch(w.direction, w.letterOrder, position, letter))
    }
}

fun handleXWordSearches(xWordSearches: MutableList<MutableList<WordSearchElement>>, actElement: WordSearchElement) {
    xWordSearches.filter { x ->  isPartOfX(x, actElement) }
        .forEach { x -> x.add(actElement) }
    if (actElement.letter.isXStartOrEnd()) {
        xWordSearches.add(mutableListOf(actElement))
    }
}

fun isPartOfX(xWord: MutableList<WordSearchElement>, actElement: WordSearchElement): Boolean {
    val countOfLetters = xWord.size
    if (countOfLetters == 2 || !actElement.letter.isXStartOrEnd()) {
        return actElement.letter == XmasLetter.A && xWord.all { it.position.isNeighbourDiagonally(actElement.position) }
    }

    when (countOfLetters) {
        1 -> return xWord.first().position.isNeighbourStraight(actElement.position, 2)
        3, 4 -> return xWord[0 + 4 - countOfLetters].isMasWith(actElement)
                    && xWord[2].position.isNeighbourDiagonally(actElement.position)
    }
    return false
}

open class WordSearchElement(
    open val position: Position,
    open val letter: XmasLetter
) {
    fun isMasWith(other: WordSearchElement): Boolean {
        return position.isNeighbourDiagonally(other.position, 2) && letter != other.letter
    }

    open fun isNeighbourByLetter(otherLetter: XmasLetter): Boolean {
        return letter.ordinal + 1 == otherLetter.ordinal
    }
}

class WordSearch(
    val direction: Direction,
    val letterOrder: Int,
    override val position: Position,
    override val letter: XmasLetter
): WordSearchElement(position, letter) {
    fun isNeighbour(otherLetter: XmasLetter, otherPosition: Position): Boolean {
        return isNeighbourByLetter(otherLetter)
                && (direction.isNeighbour(position, otherPosition))
    }

    override fun isNeighbourByLetter(otherLetter: XmasLetter): Boolean {
        return letter.ordinal + letterOrder == otherLetter.ordinal
    }
}

enum class XmasLetter {
    X,
    M,
    A,
    S;

    fun isStartOrEnd(): Boolean {
        return this == X || this == S
    }

    fun isXStartOrEnd(): Boolean {
        return this == M || this == S
    }
}
