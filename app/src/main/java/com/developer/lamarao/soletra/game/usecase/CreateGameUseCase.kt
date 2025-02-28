package com.developer.lamarao.soletra.game.usecase

import com.developer.lamarao.soletra.database.model.Game
import com.developer.lamarao.soletra.database.model.Word
import com.developer.lamarao.soletra.database.repository.DatabaseRepository
import javax.inject.Inject

class CreateGameUseCase @Inject constructor(
    private val databaseRepository: DatabaseRepository
) {

    suspend fun createGame(): Game {
        val allWords = databaseRepository.getAllWords()
        val chosenPangram = databaseRepository.getAllPangrams().random()
        val allWordsFromPangram = allWords.getAllWordsFromPangram(chosenPangram).shuffled()
        val middleLetter = chosenPangram.random()
        val remainingLetter = chosenPangram.filter { char -> char != middleLetter }
        val wordsWithOnlyMiddleLetter = allWordsFromPangram.filter { word -> word.contains(middleLetter) }.take(50)
        if (wordsWithOnlyMiddleLetter.isEmpty()) {
            createGame()
        }
        val game = Game(
            currentGuess = "",
            score = 0,
            maxScore = wordsWithOnlyMiddleLetter.size,
            middleLetter = middleLetter.toString(),
            remainingLetters = remainingLetter.toSet().map { char -> char.toString() },
            words = wordsWithOnlyMiddleLetter.map { word -> Word(word) }
        )
        databaseRepository.insertGame(game)
        return game
    }

    private fun List<String>.getAllWordsFromPangram(chosenPangram: String): List<String> {
        val regex = Regex("^[${chosenPangram}]+\$")
        return filter { word -> regex.matches(word) }
    }
}
