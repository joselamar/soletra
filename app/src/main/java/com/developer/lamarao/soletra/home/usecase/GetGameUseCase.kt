package com.developer.lamarao.soletra.home.usecase

import com.developer.lamarao.soletra.database.model.Game
import com.developer.lamarao.soletra.database.repository.DatabaseRepository
import javax.inject.Inject

class GetGameUseCase @Inject constructor(
    private val databaseRepository: DatabaseRepository
) {

    suspend fun getCurrentGame(): Game? = databaseRepository.getCurrentGame()

}