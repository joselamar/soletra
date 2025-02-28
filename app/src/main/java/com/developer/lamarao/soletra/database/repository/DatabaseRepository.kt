package com.developer.lamarao.soletra.database.repository

import com.developer.lamarao.soletra.database.dao.GameDao
import com.developer.lamarao.soletra.database.dao.PangramsDao
import com.developer.lamarao.soletra.database.dao.WordsDao
import com.developer.lamarao.soletra.database.model.Game
import javax.inject.Inject

class DatabaseRepository @Inject constructor(
    private val gameDao: GameDao,
    private val wordsDao: WordsDao,
    private val pangramsDao: PangramsDao
) {

    suspend fun getAllWords() = wordsDao.getAllWords()

    suspend fun getAllPangrams() = pangramsDao.getAllPangrams()

    suspend fun getCurrentGame() = gameDao.getGame()

    suspend fun insertGame(game: Game) = gameDao.insertGame(game)

    suspend fun updateGame(game: Game) = gameDao.updateGame(game)

    suspend fun deleteGame(game: Game) = gameDao.deleteGame(game)
}