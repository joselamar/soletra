package com.developer.lamarao.soletra.game.usecase

import com.developer.lamarao.soletra.database.model.Game
import com.developer.lamarao.soletra.database.repository.DatabaseRepository
import javax.inject.Inject

class UpdateGameUseCase @Inject constructor(
    private val databaseRepository: DatabaseRepository
) {

    suspend fun updateGame(game: Game) {
        databaseRepository.updateGame(game)
    }

}
